JFlask
======

A small web framework in java, just for practice.

#Usage:

## Route

```java
@Route("/")
public String index() {
    return "here is index ";
}


@Route("/user/<str>")
public Template showUser(String username) {
    Map<String, Object> model = new HashMap<String, Object>();
    model.put("username", username);
    return JFlask.renderTemplate("user.html", model);
}
```

## Interceptor

```java
@Intercept(5) // intercept order
public class Timer implements Interceptor {
    @Override
    public void intercept(InterceptorChain chain) throws Exception {
        long begin = System.currentTimeMillis();
        chain.doIntercept();
        long end = System.currentTimeMillis();
        System.out.println(end - begin);
    }
}
```


## Consult
[WebWind]("http://code.google.com/p/webwind/")
[Flask]("http://flask.pocoo.org/")