JActiveRecord - Java Active Record
=============

Simple implementation of Active Record for Java.

Initialize model class for "test" table
```java
package com.jactiverecord.examples;

import com.jactiverecord.ActiveRecord;

public class Test extends ActiveRecord<Test> {

}
```
Connection
```java
ConnectionManager.connect("com.mysql.jdbc.Driver", "jdbc:mysql://host/database", "user", "password");
```
Select one
```java
Test model=new Test().find(1);
System.out.println(model.get("title")); // return value of title field where id=1
```
Select many
```java
List<Test> models=new Test().findAll();
for(Test model : models){
  System.out.println(model.get("title")); // return value of title fields
}

```
Insert
```java
  Test model = new Test();
  model.set("title", "new title");
  if(model.save()){
    System.out.println(model.getId());// return new id
  }
```
Update
```java
  Test model=new Test().find(1);
  model.set("title", "new title");
  if(model.save()){
    System.out.println(model.getId());// return 1
    System.out.println(model.get("title"));// return "new title"
  }
```
Delete
```java
  Test model=new Test().find(1);
  model.delete();
```
Chaining execution
```java
new Test().where("title like ?", "test").find().set("title", "new record").save();
```
