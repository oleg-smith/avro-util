/*
 * Copyright 2022 LinkedIn Corp.
 * Licensed under the BSD 2-Clause License (the "License").
 * See License in the project root for license information.
 */

package com.linkedin.avroutil1.builder;

import com.linkedin.avroutil1.compatibility.AvroCodecUtil;
import com.linkedin.avroutil1.compatibility.RandomRecordGenerator;
import com.linkedin.avroutil1.compatibility.RecordGenerationConfig;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.avro.generic.IndexedRecord;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


public class SpecificRecordTest {

  @DataProvider
  private Object[][] TestRoundTripSerializationProvider() {
    return new Object[][]{
        {vs14.SimpleRecord.class, vs14.SimpleRecord.getClassSchema()},
        {vs19.SimpleRecord.class, vs19.SimpleRecord.getClassSchema()},

        {vs14.MoneyRange.class, vs14.MoneyRange.getClassSchema()},
        {vs19.MoneyRange.class, vs19.MoneyRange.getClassSchema()},

        {vs14.DollarSignInDoc.class, vs14.DollarSignInDoc.getClassSchema()},
        {vs19.DollarSignInDoc.class, vs19.DollarSignInDoc.getClassSchema()},

        {vs14.RecordDefault.class, vs14.RecordDefault.getClassSchema()},
        {vs19.RecordDefault.class, vs19.RecordDefault.getClassSchema()},

        {vs14.ArrayOfRecords.class, vs14.ArrayOfRecords.getClassSchema()},
        {vs19.ArrayOfRecords.class, vs19.ArrayOfRecords.getClassSchema()},

        {vs14.ArrayOfStringRecord.class, vs14.ArrayOfStringRecord.getClassSchema()},
        {vs19.ArrayOfStringRecord.class, vs19.ArrayOfStringRecord.getClassSchema()}
    };
  }

  @Test(dataProvider = "TestRoundTripSerializationProvider")
  public <T extends IndexedRecord> void testRoundTripSerialization(Class<T> clazz, org.apache.avro.Schema classSchema) throws Exception {
    RandomRecordGenerator generator = new RandomRecordGenerator();
    T instance = generator.randomSpecific(clazz, RecordGenerationConfig.newConfig().withAvoidNulls(true));
    byte[] serialized = AvroCodecUtil.serializeBinary(instance);
    T deserialized = AvroCodecUtil.deserializeAsSpecific(serialized, classSchema, clazz);
    Assert.assertNotSame(deserialized, instance);
    Assert.assertEquals(deserialized, instance);
  }

  @DataProvider
  private Object[][] testSpecificRecordBuilderProvider19() {
    RandomRecordGenerator generator = new RandomRecordGenerator();
    vs19.Amount amount1 = generator.randomSpecific(vs19.Amount.class);
    vs19.Amount amount2 = generator.randomSpecific(vs19.Amount.class);
    vs19.Amount amount3 = generator.randomSpecific(vs19.Amount.class);

    vs19.RandomFixedName fixedName = generator.randomSpecific(vs19.RandomFixedName.class);

    Map<String, String> stringMap = new HashMap<>();
    Map<String, vs19.Amount> amountMap = new HashMap<>();
    stringMap.put("isTrue", "false");
    amountMap.put("amount3", amount3);

    int wierdUnionVal2 = 2;
    long wierdUnionVal3 = 4L;
    String wierdUnionVal4 = "WierdVal";
    vs19.Amount wierdUnionVal5 = generator.randomSpecific(vs19.Amount.class);;
    vs19.RandomFixedName wierdUnionVal6 = generator.randomSpecific(vs19.RandomFixedName.class);;

    return new Object[][]{
        {
          "str", "pck", Float.valueOf("1"), Double.valueOf("2"), false, Arrays.asList("123", "123"), amount1,
            Arrays.asList(amount1, amount2), stringMap, amountMap, null, fixedName, null
        },
        {
            "str", "pck", Float.valueOf("1"), Double.valueOf("2"), false, Arrays.asList("123", "123"), amount1,
            Arrays.asList(amount1, amount2), stringMap, amountMap, null, fixedName, wierdUnionVal2
        },
        {
            "str", "pck", Float.valueOf("1"), Double.valueOf("2"), false, Arrays.asList("123", "123"), amount1,
            Arrays.asList(amount1, amount2), stringMap, amountMap, null, fixedName, wierdUnionVal3
        },
        {
            "str", "pck", Float.valueOf("1"), Double.valueOf("2"), false, Arrays.asList("123", "123"), amount1,
            Arrays.asList(amount1, amount2), stringMap, amountMap, null, fixedName, wierdUnionVal4
        },
        {
            "str", "pck", Float.valueOf("1"), Double.valueOf("2"), false, Arrays.asList("123", "123"), amount1,
            Arrays.asList(amount1, amount2), stringMap, amountMap, null, fixedName, wierdUnionVal5
        },
        {
            "str", "pck", Float.valueOf("1"), Double.valueOf("2"), false, Arrays.asList("123", "123"), amount1,
            Arrays.asList(amount1, amount2), stringMap, amountMap, null, fixedName, wierdUnionVal6
        },

    };
  }

  @Test(dataProvider = "testSpecificRecordBuilderProvider19")
  public void testSpecificRecordBuilder19(String stringField, String package$, Float exception, Double dbl,
      Boolean isTrue, List<CharSequence> arrayOfStrings, vs19.Amount min, List<vs19.Amount> arrayOfRecord,
      Map<CharSequence, CharSequence> mapOfStrings, Map<CharSequence, vs19.Amount> mapOfRecord, vs19.Amount simpleUnion,
      vs19.RandomFixedName fixedType, Object wierdUnion) throws Exception {
    vs19.BuilderTester builderTester = vs19.BuilderTester.Builder.newBuilder()
        .setStringField(stringField)
        .setPackage$(package$)
        .setException(exception)
        .setDbl(dbl)
        .setIsTrue(isTrue)
        .setArrayOfStrings(arrayOfStrings)
        .setMin(min)
        .setArrayOfRecord(arrayOfRecord)
        .setMapOfStrings(mapOfStrings)
        .setMapOfRecord(mapOfRecord)
        .setSimpleUnion(simpleUnion)
        .setFixedType(fixedType)
        .setWierdUnion(wierdUnion).build();
    Assert.assertNotNull(builderTester);

    Assert.assertEquals(builderTester.get(0), stringField);
    Assert.assertEquals(builderTester.get(1), package$);
    Assert.assertEquals(builderTester.get(2), exception);
    Assert.assertEquals(builderTester.get(3), dbl);
    Assert.assertEquals(builderTester.get(4), isTrue);
    Assert.assertEquals(builderTester.get(5), arrayOfStrings);
    Assert.assertEquals(builderTester.get(6), min);
    Assert.assertEquals(builderTester.get(7), arrayOfRecord);
    Assert.assertEquals(builderTester.get(8), mapOfStrings);
    Assert.assertEquals(builderTester.get(9), mapOfRecord);
    Assert.assertEquals(builderTester.get(10), simpleUnion);
    Assert.assertEquals(builderTester.get(11), fixedType);
    Assert.assertEquals(builderTester.get(12), wierdUnion);
  }

  @DataProvider
  private Object[][] testSpecificRecordBuilderProvider14() {
    RandomRecordGenerator generator = new RandomRecordGenerator();
    vs14.Amount amount1 = generator.randomSpecific(vs14.Amount.class);
    vs14.Amount amount2 = generator.randomSpecific(vs14.Amount.class);
    vs14.Amount amount3 = generator.randomSpecific(vs14.Amount.class);

    vs14.RandomFixedName fixedName = generator.randomSpecific(vs14.RandomFixedName.class);

    Map<String, String> stringMap = new HashMap<>();
    Map<String, vs14.Amount> amountMap = new HashMap<>();
    stringMap.put("isTrue", "false");
    amountMap.put("amount3", amount3);

    int wierdUnionVal2 = 2;
    long wierdUnionVal3 = 4L;
    String wierdUnionVal4 = "WierdVal";
    vs14.Amount wierdUnionVal5 = generator.randomSpecific(vs14.Amount.class);;
    vs14.RandomFixedName wierdUnionVal6 = generator.randomSpecific(vs14.RandomFixedName.class);;

    return new Object[][]{
        {
            "str", "pck", Float.valueOf("1"), Double.valueOf("2"), false, Arrays.asList("123", "123"), amount1,
            Arrays.asList(amount1, amount2), stringMap, amountMap, null, fixedName, null
        },
        {
            "str", "pck", Float.valueOf("1"), Double.valueOf("2"), false, Arrays.asList("123", "123"), amount1,
            Arrays.asList(amount1, amount2), stringMap, amountMap, null, fixedName, wierdUnionVal2
        },
        {
            "str", "pck", Float.valueOf("1"), Double.valueOf("2"), false, Arrays.asList("123", "123"), amount1,
            Arrays.asList(amount1, amount2), stringMap, amountMap, null, fixedName, wierdUnionVal3
        },
        {
            "str", "pck", Float.valueOf("1"), Double.valueOf("2"), false, Arrays.asList("123", "123"), amount1,
            Arrays.asList(amount1, amount2), stringMap, amountMap, null, fixedName, wierdUnionVal4
        },
        {
            "str", "pck", Float.valueOf("1"), Double.valueOf("2"), false, Arrays.asList("123", "123"), amount1,
            Arrays.asList(amount1, amount2), stringMap, amountMap, null, fixedName, wierdUnionVal5
        },
        {
            "str", "pck", Float.valueOf("1"), Double.valueOf("2"), false, Arrays.asList("123", "123"), amount1,
            Arrays.asList(amount1, amount2), stringMap, amountMap, null, fixedName, wierdUnionVal6
        },

    };
  }

  @Test(dataProvider = "testSpecificRecordBuilderProvider14")
  public void testSpecificRecordBuilder14(String stringField, String package$, Float exception, Double dbl,
      Boolean isTrue, List<CharSequence> arrayOfStrings, vs14.Amount min, List<vs14.Amount> arrayOfRecord,
      Map<CharSequence, CharSequence> mapOfStrings, Map<CharSequence, vs14.Amount> mapOfRecord, vs14.Amount simpleUnion,
      vs14.RandomFixedName fixedType, Object wierdUnion) throws Exception {
    vs14.BuilderTester builderTester = vs14.BuilderTester.Builder.newBuilder()
        .setStringField(stringField)
        .setPackage$(package$)
        .setException(exception)
        .setDbl(dbl)
        .setIsTrue(isTrue)
        .setArrayOfStrings(arrayOfStrings)
        .setMin(min)
        .setArrayOfRecord(arrayOfRecord)
        .setMapOfStrings(mapOfStrings)
        .setMapOfRecord(mapOfRecord)
        .setSimpleUnion(simpleUnion)
        .setFixedType(fixedType)
        .setWierdUnion(wierdUnion).build();
    Assert.assertNotNull(builderTester);

    Assert.assertEquals(builderTester.get(0), stringField);
    Assert.assertEquals(builderTester.get(1), package$);
    Assert.assertEquals(builderTester.get(2), exception);
    Assert.assertEquals(builderTester.get(3), dbl);
    Assert.assertEquals(builderTester.get(4), isTrue);
    Assert.assertEquals(builderTester.get(5), arrayOfStrings);
    Assert.assertEquals(builderTester.get(6), min);
    Assert.assertEquals(builderTester.get(7), arrayOfRecord);
    Assert.assertEquals(builderTester.get(8), mapOfStrings);
    Assert.assertEquals(builderTester.get(9), mapOfRecord);
    Assert.assertEquals(builderTester.get(10), simpleUnion);
    Assert.assertEquals(builderTester.get(11), fixedType);
    Assert.assertEquals(builderTester.get(12), wierdUnion);
  }
}
