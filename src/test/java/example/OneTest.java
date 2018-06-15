package example;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class OneTest {

  @Before
  public void beforeFoo(){
    File file = new File("out.txt");
    if(file.exists()){
      file.delete();
    }
  }

  private List<String> readOutFile() {
    File file = new File("out.txt");
    FileInputStream is;
    List<String> list = new ArrayList<String>();
    try {
      if (file.length() != 0) {
        is = new FileInputStream(file);
        InputStreamReader streamReader = new InputStreamReader(is);
        BufferedReader reader = new BufferedReader(streamReader);
        String line;
        while ((line = reader.readLine()) != null) {
          list.add(line);
        }
        reader.close();
        is.close();
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
    return list;

  }

  @Test
  public void testFoo() {
    Long start=System.currentTimeMillis();
    One one = new One();
    one.foo();
    List<String> list = readOutFile();

    Long end=System.currentTimeMillis();
    System.out.print(end-start);
  }

}