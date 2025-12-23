package annotation.basic.inherited;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;

@Inherited
@Retention( java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface InheritedAnnotation {
}
