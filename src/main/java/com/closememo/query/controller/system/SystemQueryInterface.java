package com.closememo.query.controller.system;

import com.closememo.query.config.openapi.apitags.SystemApiTag;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SystemApiTag
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@PreAuthorize("hasRole('SYSTEM')")
@Validated
@RequestMapping("/query/system")
@RestController
public @interface SystemQueryInterface {

}
