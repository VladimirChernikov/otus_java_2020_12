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
    stompClient.heartbeat_out = 5000;
    stompClient.connect({}, (frame) => {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/status/' + sessionId, (message) => refreshStatus(message.body));
        stompClient.subscribe('/topic/clients',             (message) => refreshTable(message.body) );
    });

}

// const disconnect = () => {
//     if (stompClient !== null) {
//         stompClient.disconnect();
//     }
//     setConnected(false);
//     console.log("Disconnected");
// }

const refreshTable = (message) => $("#statusContainer").append("<tr><td>" + message + "</td></tr>")

const refreshStatus = (message) => $("#statusContainer").append("<tr><td>" + message + "</td></tr>")

const addClient = () => {
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


$(function () {
    $("form").on('submit', (event) => {
        event.preventDefault();
    });
    $("#addClient").click(addClient);
    connect();
});
