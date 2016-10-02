/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.mnemonic;
import java.nio.ByteBuffer;
import java.util.zip.CRC32;
import java.util.zip.Checksum;
/**
 *
 *
 */

@DurableEntity
public abstract class Person<E> implements Durable, Comparable<Person<E>> {
  E element;

  @Override
  public void initializeAfterCreate() {
    System.out.println("Initializing After Created");
  }

  @Override
  public void initializeAfterRestore() {
    System.out.println("Initializing After Restored");
  }

  @Override
  public void setupGenericInfo(EntityFactoryProxy[] efproxies, DurableType[] gftypes) {

  }

  public void testOutput() throws RetrieveDurableEntityError {
    System.out.printf("Person %s, Age: %d ( %s ) \n", getName(), getAge(),
        null == getMother() ? "No Recorded Mother" : "Has Recorded Mother");
    ByteBuffer mres = (ByteBuffer) getPicture().get();
    byte bytes[] = new byte[mres.capacity()];
    mres.get(bytes, 0, bytes.length);
    Checksum checksum = new CRC32();
    checksum.update(bytes, 0, bytes.length);
    long checksumValue = checksum.getValue();
    System.out.println("Size of Picture buffer: " + checksumValue);
  }

  public int compareTo(Person<E> anotherPerson) {
    int ret = 0;
    if (0 == ret) {
      ret = getAge().compareTo(anotherPerson.getAge());
    }
    if (0 == ret) {
      ret = getName().compareTo(anotherPerson.getName());
    }
    if (0 == ret) {
      ret = (int) (getPicture().getSize() - anotherPerson.getPicture().getSize());
    }
    if (0 == ret) {
      ret = (int) (getFingerprint().getSize() - anotherPerson.getFingerprint().getSize());
    }
 
    return ret;
  }

  @DurableGetter
  public abstract Short getAge();

  @DurableSetter
  public abstract void setAge(Short age);

  @DurableGetter
  public abstract String getName() throws RetrieveDurableEntityError;

  @DurableSetter
  public abstract void setName(String name, boolean destroy)
      throws OutOfHybridMemory, RetrieveDurableEntityError;

  @DurableGetter
  public abstract MemBufferHolder getPicture();

  @DurableSetter
  public abstract void setPicture(MemBufferHolder mbh, boolean destroy);

  @DurableGetter
  public abstract MemChunkHolder getFingerprint();

  @DurableSetter
  public abstract void setFingerprint(MemChunkHolder mch, boolean destroy);

  @DurableGetter
  public abstract Person<E> getMother() throws RetrieveDurableEntityError;

  @DurableSetter
  public abstract void setMother(Person<E> mother, boolean destroy) throws RetrieveDurableEntityError;

  @DurableGetter
  public abstract Person<E> getFather() throws RetrieveDurableEntityError;

  @DurableSetter
  public abstract void setFather(Person<E> mother, boolean destroy) throws RetrieveDurableEntityError;
}
