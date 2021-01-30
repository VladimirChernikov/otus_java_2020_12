package ru.otus.hw02.customer;


import java.util.AbstractMap;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class CustomerService {

    //todo: 3. надо реализовать методы этого класса
    //важно подобрать подходящую Map-у, посмотрите на редко используемые методы, они тут полезны
    
    final private NavigableMap<Customer, String> customerMap = new TreeMap<>( Comparator.comparing( Customer::getScores ) );

    public Map.Entry<Customer, String> getSmallest() {
        //Возможно, чтобы реализовать этот метод, потребуется посмотреть как Map.Entry сделан в jdk
        return getEntryCopy( customerMap.firstEntry() );
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        return customerMap.higherEntry(customer);
    }

    public void add(Customer customer, String data) {
        customerMap.put(customer, data);
    }

    /**
     * Creates deep copy of entry.
     *
     * @param entry
     * @return clone of entry
     */
    private Map.Entry<Customer, String> getEntryCopy( Map.Entry<Customer, String> entry ) {
        return new AbstractMap.SimpleImmutableEntry<Customer, String>( new Customer( entry.getKey() ), entry.getValue() );
    }

}
