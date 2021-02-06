package ru.otus.hw03;

/**
 * DIY tester class runner.
 *
 */
public class Runner
{
    public static void main(String[] args)  {
        if ( args.length != 1 ) {
            System.err.println("Usage: full.path.to.TestClass");
        } else {
            try {
                DiyTester diyTester = new DiyTester(args[0]);
                diyTester.runAllTests();
                diyTester.report();
            } catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}


