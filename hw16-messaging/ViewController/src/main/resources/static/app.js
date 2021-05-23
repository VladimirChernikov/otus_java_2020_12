let stompClient = null;
let sessionId = String(Math.random()).substring(2,18);

const setConnected = (connected) => {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#chatLine").show();
    } else {
        $("#chatLine").hide();
    }
    $("#message").html("");
}

const connect = () => {
    stompClient = Stomp.over( new SockJS('/websocket') );
    stompClient.reconnect_delay = 5000;
    stompClient.debug = false;
    stompClient.heartbeat_in = 0;
    stompClient.heartbeat_out = 20000;
    stompClient.connect({}, (frame) => {
        setConnected(true);
        prependStatus('Connected: ' + frame);
        stompClient.subscribe('/topic/status/' + sessionId, (message) => prependStatus(message.body));
        stompClient.subscribe('/topic/clients/'+ sessionId, (message) => showClient(message.body));
        stompClient.subscribe('/topic/clients',             (message) => prependClient(message.body));
    },
        (message) => {
            prependStatus(message + " " + "Trying to reconnect in 10 seconds...");
            setTimeout( "connect()", 10000 );
        }
    );

}

// const disconnect = () => {
//     if (stompClient !== null) {
//         stompClient.disconnect();
//     }
//     setConnected(false);
//     console.log("Disconnected");
// }

const prependStatus = (message) => $("#statusContainer").prepend( $( "<tr><td>" + new Date().toJSON() + " " + message + "</td></tr>" ).hide().delay(500).show('slow') )

const clientToTableRow = (client) => {
    rowHTML = "<tr>";
    rowHTML += "<td>"+client.id+"</td>";
    rowHTML += "<td>"+client.name+"</td>";
    if ( client.phones === undefined ) {
        rowHTML += "<td></td>";
    } else {
        rowHTML += "<td>"+client.phones.map(e => e.number).join(';')+"</td>";
    }
    if ( client.address === undefined ) {
        rowHTML += "<td></td>";
    } else {
        rowHTML += "<td>"+client.address.street+"</td>";
    }
    return rowHTML;
}

const prependClient = (message) => $("#clientsTable").prepend( $( clientToTableRow( JSON.parse(message) ) ).hide().delay(500).show('slow') );

const showClient = (message) => $("#clientContainer").append( $( clientToTableRow( JSON.parse(message) ) ).hide().delay(500).show('slow') );

const putClient = () => {
    //////////////
    client = {};
    client.name = $('#clientNameTextBox').val();
    client.phones = $('#clientPhoneTextBox').val()
        .split(';').map( el => { phone = {}; phone.number = el; return phone } );
    address = {};
    address.street = $('#clientAddressTextBox').val();
    client.address = address;

    //////////////
    stompClient.send("/app/client", {'sessionId':sessionId}, JSON.stringify(client));
    //////////////
}

const getClientById = () => {
    id = $('#getClientId').val();
    $("#clientContainer").empty();
    stompClient.send("/app/client."+id, {'sessionId':sessionId});
}

$(function () {
    $("form").on('submit', (event) => {
        event.preventDefault();
    });
    $("#putClient").click(putClient);
    $("#getClientById").click(getClientById);
    connect();
});
