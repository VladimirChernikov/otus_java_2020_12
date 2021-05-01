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

    private static final Class<AppComponentsContainerConfig> CONFIG_ANNOTATION = AppComponentsContainerConfig.class;
    private static final Class<AppComponent> METHOD_ANNOTATION = AppComponent.class;
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        return (C)this.appComponentsByName.values().stream()
                                          .filter( componentClass::isInstance )
                                          .findAny().orElse(null);
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        return (C)this.appComponentsByName.get( componentName );
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);

        List<Method> components = this.getComponentConstructors( configClass.getMethods() );

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
            throw new RuntimeException(String.format("Cannot initialize dependent components (%s) for class config %s", components, configClass.getName()));
        }
    }

    private List<Method> getComponentConstructors( Method[] methods ) {
        return Arrays.stream( methods )
              // methods with AppComponent annotation 
              .filter( method -> method.isAnnotationPresent( METHOD_ANNOTATION ) )
              // sorted by Order value
              .sorted( (m1,m2) -> this.getComponentOrder(m1) - this.getComponentOrder(m2) )
              .collect( Collectors.toList() );
    }

    private int getComponentOrder( Method componentConstructor ) {
        return componentConstructor.getAnnotation( METHOD_ANNOTATION ).order();
    }

    private String getComponentName( Method componentConstructor ) {
        return componentConstructor.getAnnotation( METHOD_ANNOTATION ).name();
    }

    private Object createComponentObject( Object configInstance, Method componentConstructor ) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
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
