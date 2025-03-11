package org.example.newsfeed.domain.auth.anotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER) // 너 어디가서 붙을래?
@Retention(RetentionPolicy.RUNTIME) // 표지판으로서 역할을 하려면 Runtime (동작시점) 애도 살아있어야함
public @interface Auth {
}
