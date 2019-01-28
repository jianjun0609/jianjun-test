package com.jianjun.jianjuntest.configurer;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class Swagger2 {

	@Value("${swagger.host}")
	private String swaggerHost;
	@Value("${swagger.enable}")
	private boolean enableSwagger;

	@Bean
	public Docket createRestApi() {
		/*ParameterBuilder tokenPar = new ParameterBuilder();
		List<Parameter> pars = new ArrayList<Parameter>();
		tokenPar.name("X-Access-Token").description("令牌").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
		ParameterBuilder tokenPar1 = new ParameterBuilder();
		tokenPar1.name("client").description("客户端").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
		pars.add(tokenPar.build());
		pars.add(tokenPar1.build());*/

		return new Docket(DocumentationType.SWAGGER_2).host(swaggerHost).apiInfo(apiInfo()).select()
				.apis(basePackage("com.jianjun.jianjuntest")).paths(PathSelectors.any())
				.build().enable(enableSwagger);
	}

	@SuppressWarnings("deprecation")
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("建军测试专用").description("").termsOfServiceUrl("").contact("test@jianjun.com")
				.version("1.0").build();
	}

	/**
	 * Predicate that matches RequestHandler with given base package name for the class of the handler method.
	 * This predicate includes all request handlers matching the provided basePackage
	 *
	 * @param basePackage - base package of the classes
	 * @return this
	 */
	public static Predicate<RequestHandler> basePackage(final String basePackage) {
		return new Predicate<RequestHandler>() {

			@Override
			public boolean apply(RequestHandler input) {
				return declaringClass(input).transform(handlerPackage(basePackage)).or(true);
			}
		};
	}

	/**
	 * 处理包路径配置规则,支持多路径扫描匹配以逗号隔开
	 *
	 * @param basePackage 扫描包路径
	 * @return Function
	 */
	private static Function<Class<?>, Boolean> handlerPackage(final String basePackage) {
		return new Function<Class<?>, Boolean>() {

			@Override
			public Boolean apply(Class<?> input) {
				for (String strPackage : basePackage.split(",")) {
					boolean isMatch = input.getPackage().getName().startsWith(strPackage);
					if (isMatch) {
						return true;
					}
				}
				return false;
			}
		};
	}

	/**
	 * @param input RequestHandler
	 * @return Optional
	 */
	private static Optional<? extends Class<?>> declaringClass(RequestHandler input) {
		return Optional.fromNullable(input.declaringClass());
	}
}
