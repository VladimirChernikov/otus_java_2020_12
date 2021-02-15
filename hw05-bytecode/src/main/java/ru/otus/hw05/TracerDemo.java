package ru.otus.hw05;

/*

    example output:
    CALL secureAccess(param: 'Security Param', x: '10')
    secureAccess, param:Security Param x 
    RET '20' <- secureAccess(param: 'Security Param', x: '10')

*/
public class TracerDemo {

    public static void main(String[] args) {
        MyClassImpl myClass = new MyClassImpl();
        myClass.secureAccess("Security Param",10);
    }

}
