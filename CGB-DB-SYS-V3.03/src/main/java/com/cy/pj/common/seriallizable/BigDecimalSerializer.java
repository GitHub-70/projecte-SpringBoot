package com.cy.pj.common.seriallizable;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * BigDecimal 序列化器
 *      可使用 ObjectMapper.registerModule(module) 注册到 ObjectMapper
 *      也可对指定字段使用 @JsonSerialize(using = BigDecimalSerializer.class)
 */
public class BigDecimalSerializer extends JsonSerializer<BigDecimal> {
    @Override
    public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(value.setScale(2, RoundingMode.HALF_UP).toString());
    }
}

    // 注册到 ObjectMapper
//    ObjectMapper mapper = new ObjectMapper();
//    SimpleModule module = new SimpleModule();
//module.addSerializer(BigDecimal.class, new BigDecimalSerializer());
//        mapper.registerModule(module);

