//
// Copyright (C) 2005 United States Government as represented by the
// Administrator of the National Aeronautics and Space Administration
// (NASA).  All Rights Reserved.
// 
// This software is distributed under the NASA Open Source Agreement
// (NOSA), version 1.3.  The NOSA has been approved by the Open Source
// Initiative.  See the file NOSA.txt at the top of the distribution
// directory tree for the complete NOSA document.
// 
// THE SUBJECT SOFTWARE IS PROVIDED "AS IS" WITHOUT ANY WARRANTY OF ANY
// KIND, EITHER EXPRESSED, IMPLIED, OR STATUTORY, INCLUDING, BUT NOT
// LIMITED TO, ANY WARRANTY THAT THE SUBJECT SOFTWARE WILL CONFORM TO
// SPECIFICATIONS, ANY IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR
// A PARTICULAR PURPOSE, OR FREEDOM FROM INFRINGEMENT, ANY WARRANTY THAT
// THE SUBJECT SOFTWARE WILL BE ERROR FREE, OR ANY WARRANTY THAT
// DOCUMENTATION, IF PROVIDED, WILL CONFORM TO THE SUBJECT SOFTWARE.
//
package gov.nasa.alsUtility;
import java.util.Vector;
import java.util.Enumeration;
import java.lang.Integer;

/**
Extends the capabilities of Vector.
*/
public class ExtendedVector extends Vector {

public ExtendedVector() {}
/** add Integer objects corresponding to array */
public ExtendedVector(int[] array) {
    for(int i = 0; i < array.length; i++)
        add(new Integer(array[i]));
}
public ExtendedVector(Object[] array) {
    for(int i = 0; i < array.length; i++)
        add(array[i]);
}

public ExtendedVector copy() {
    ExtendedVector copy = new ExtendedVector();
    for(int i = 0; i < size(); i++)
        copy.add(get(i));
    return copy;
}
/** are the elements equals()? */
public boolean isEqual(Vector other) {
    if (size() != other.size())
        return false;
    for(int i = 0; i < size(); i++)
        if (!get(i).equals(other.get(i)))
            return false;
    return true;
}
/**
create an ExtendedVector with the elements from Enumeration e
*/
public ExtendedVector(Enumeration e) {
    while(e.hasMoreElements()) {addElement(e.nextElement());}
}

/**
sorts elements according to their string representations
*/
public void sortByString() { //  slow sort of bubble sort
    boolean sorted = false;
    while (!sorted) {
    	sorted = true;
    	for (int i = 0; i + 1 < size(); i++){
    	    if (0 < (elementAt(i)).toString().compareTo(elementAt(i + 1).toString())) {
    		    String temporary = elementAt(i).toString();
    		    setElementAt(elementAt(i + 1),i);
    		    setElementAt(temporary,i + 1);
    		    sorted = false;
    	    }
    	}
    }
}
public Object getRandomElement() {
    if (size() == 0) return null;
    return elementAt(RandomNumber.getIndex(size()));
}
/**
return a random element that satisfies Predicate p
*/
public Object getRandomElement(Predicate p) {
    return findAll(p).getRandomElement();
}
/**
addElement the elements in Vector v
*/
public void addVector(Vector v) {
    for(int i = 0; i < v.size(); i++){
        addElement(v.elementAt(i));
    }
}
public void append(Vector v) {addVector(v);}
/**
remove all elements that satisfy Predicate p
*/
public void removeAll(Predicate p) {
    for(int i = size()-1; i >= 0; i--){
        if (p.execute(elementAt(i)))
            removeElementAt(i);
    }
}
/**
@return an ExtendedVector with all the elements that satisfy Predicate p
*/
public ExtendedVector findAll(Predicate p) {
    ExtendedVector vector = new ExtendedVector();
    for(int i = 0; i < size(); i++){
        if (p.execute(elementAt(i)))
            vector.addElement(elementAt(i));
    }
    return vector;
}
/**
execute Procedure p on all elements
*/
public void executeOnAll(Procedure p) {
    for(int i = 0; i < size(); i++){
        p.execute(elementAt(i));
    }
}
/**
@return true if at least one element satisfies Predicate predicate. Predicate is not guaranteed
to be executed on all elements.
*/
public boolean check(Predicate predicate) {return check(predicate,1);}
/**
@return true if at least number elements satisfies Predicate predicate. Predicate is not guaranteed
to be executed on all elements.
*/
public boolean check(Predicate predicate, int number) {
    if (number <= 0) return true;
    int soFar = 0;
    for(int i = 0; i < size(); i++){
        if (predicate.execute(elementAt(i))) {
            soFar++;
            if (soFar >= number) return true;
        }
    }
    return false;
}
public void insertBefore(int index, Vector toInsert) {
    Error.assertTrue(0 <= index && index < size());
    for(int i = toInsert.size()-1; i >= 0; i--)
        add(index,toInsert.get(i));
}
public void insertAfter(int index, Vector toInsert) {
    Error.assertTrue(0 <= index && index < size());
    if (index == size() - 1)
        for(int i = 0; i < toInsert.size(); i++)
            add(toInsert.get(i));
    else
        for(int i = toInsert.size()-1; i >= 0; i--)
            add(index+1,toInsert.get(i));
}

}
