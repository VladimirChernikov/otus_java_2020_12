package ru.otus.appcontainer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final static Class<AppComponentsContainerConfig> CONFIG_ANNOTATION = AppComponentsContainerConfig.class;
    private final static Class<AppComponent> METHOD_ANNOTATION = AppComponent.class;
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        return (C)this.appComponentsByName.values().stream()
                                          .filter( component -> componentClass.isInstance( component ) )
                                          .findAny().orElse(null);
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        return (C)this.appComponentsByName.get( componentName );
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);

        // find constructors
        List<Method> components = Arrays.stream( configClass.getMethods() )
              // methods with AppComponent annotation 
              .filter( method -> method.isAnnotationPresent( METHOD_ANNOTATION ) )
              // sorted by Order value
              .sorted( (m1,m2) -> this.getComponentOrder(m1) - this.getComponentOrder(m2) )
              .collect( Collectors.toList() );

        // initialize objects
        try {
            Object configInstance = configClass.getDeclaredConstructors()[0].newInstance();
            for ( var component : components ) {
                String componentName = this.getComponentName( component );
                Object componentObject;
                componentObject = this.createComponentObject( configInstance, component );
                this.appComponentsByName.put( componentName, componentObject );
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private int getComponentOrder( Method componentConstructor ) {
        int results = componentConstructor.getAnnotation( METHOD_ANNOTATION ).order();
        return results;
    }

    private String getComponentName( Method componentConstructor ) {
        String results = componentConstructor.getAnnotation( METHOD_ANNOTATION ).name();
        return results;
    }

    private Object createComponentObject( Object configInstance, Method componentConstructor ) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
        Object result;
        Object[] parameters = Arrays.stream( componentConstructor.getParameters() )
            .map( p -> this.getAppComponent(p.getType()) )
            .toArray();
        result = componentConstructor.invoke( configInstance, parameters );
        return result;    
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent( CONFIG_ANNOTATION )) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

}
