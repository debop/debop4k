package debop4k.benchmark.models;//package debop4k.benchmark.models;
//
//
//import lombok.Data;
//
//import java.io.Serializable;
//
//
//public class Cell implements Serializable {
//
//  public static Cell of() {
//    return new Cell();
//  }
//
//  public static Cell of(int r, int c) {
//    return new Cell(r, c);
//  }
//
//  public Cell() {this(0, 0);}
//
//  public Cell(int r, int c) {
//    this.row = r;
//    this.col = c;
//  }
//
//  private int row = 0;
//  private int col = 0;
//
//  private String text = null;
//  private byte[] bytes = null;
//
//  public int getRow() {
//    return row;
//  }
//
//  public int getCol() {
//    return col;
//  }
//
//
//  public String getText() {
//    return text;
//  }
//
//  public void setText(String text) {
//    this.text = text;
//  }
//
//  public byte[] getBytes() {
//    return bytes;
//  }
//
//  public void setBytes(byte[] bytes) {
//    this.bytes = bytes;
//  }
//}
