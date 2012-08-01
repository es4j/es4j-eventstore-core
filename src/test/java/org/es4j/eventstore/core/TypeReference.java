package org.es4j.eventstore.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;


/**
 * References a generic type. Original implementation was done by Bob.
 * @author crazybob@google.com (Bob Lee)
 */

/**
 * Comments were added by Minkoo.
 * @author minkoo.seo@gmail.com (Minkoo Seo)
 */
// This is abstract class, so it can't be instantiated.
abstract class TypeReference<T> {

    // Type is the super interface of all types in Java. These include raw
    // types, parameterized types, array types, type variables and primitive
    // types.
    private final Type type;
    private Constructor<?> constructor;

    protected TypeReference() {

        // Give me class information.
        Class<?> klass = getClass();

        // See that I have created an anonymous subclass of TypeReference in the
        // main method. Hence, to get the TypeReference itself, I need superclass.
        // Furthermore, to get Type information, you should call
        // getGenericSuperclass() instead of getSuperclass().
        Type superClass = klass.getGenericSuperclass();

        if (superClass instanceof Class) {

            // Type has four subinterface:
            // (1) GenericArrayType: component type is either a
            // parameterized type or a type variable. Parameterized type is a class
            // or interface with its actual type argument, e.g., ArrayList<String>.
            // Type variable is unqualified identifier like T or V.
            //
            // (2) ParameterizedType: see (1).
            //
            // (3) TypeVariable<D>: see (1).
            //
            // (4) WildcardType: ?
            //
            // and one subclass:
            // (5) Class.
            //
            // If TypeReference is created by 'new TypeReference() { }', then
            // superClass would be just an instance of Class instead of one of the
            // interfaces described above. In that case, because I don't have type
            // passed to TypeReference, an exception should be raised.
            throw new RuntimeException("Missing Type Parameter");
        }

        // By superClass, we mean 'TypeReference<T>'. So, it is obvious that
        // superClass is ParameterizedType.
        ParameterizedType pt = (ParameterizedType) superClass;

        // We have one type argument in TypeRefence<T>: T.
        type = pt.getActualTypeArguments()[0];
    }

    public Type getType() {
        return type;
    }

    @SuppressWarnings("unchecked")
    public T newInstance() throws NoSuchMethodException, IllegalAccessException,
                                  InvocationTargetException, InstantiationException {
        if (constructor == null) {
            // If T is raw type, we are happy with that. Otherwise, we get the raw
            // type of T. For example, if T is String, we are done. If T is
            // ArrayList<String>, we get ArrayList.
            Class<?> rawType = type instanceof Class<?>
                    ? (Class<?>) type
                    : (Class<?>) ((ParameterizedType) type).getRawType();
            constructor = rawType.getConstructor();
        }

        // Create an instance of T. If T is raw type, no problem. But, if T is
        // parameterized type, we are actually creating a raw type instance of it,
        // and we need to cast it into T which is parameterized type. In that
        // case, (T) actually casts the raw type into parameterized type, but note
        // that this raises an unchecked warning. Unchecked warning is raised when
        // we cast something with type parameter or parameterized type.
        //
        // Since it is pretty much obvious that newInstance actually creates the
        // instance of T, unchecked warning is suppressed in the method signature.
        return (T) constructor.newInstance();
    }
}

/*
public class SuperTypeTokenDemo {
  public static void main(String[] args) {
    // Note that an anonymous sublcass instance of TypeReference is being
    // created. Because TypeReference itself is an abstract class, I should
    // get the instance of the subclass.
    TypeReference tr = new TypeReference<ArrayList<String>>() { };
    TypeReference tr2 = new TypeReference<String>() { };
  }
}
*/