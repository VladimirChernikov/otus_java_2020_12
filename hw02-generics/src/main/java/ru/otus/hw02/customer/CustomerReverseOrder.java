package ru.otus.hw02.customer;

import java.util.ArrayDeque;
import java.util.Deque;

public class CustomerReverseOrder {

    //todo: 2. надо реализовать методы этого класса
    //надо подобрать подходящую структуру данных, тогда решение будет в "две строчки"
    
    final private Deque<Customer> customerStack = new ArrayDeque<>();

    public void add(Customer customer) {
        customerStack.push(customer);
    }

    public Customer take() {
        return customerStack.pop();
    }
}
