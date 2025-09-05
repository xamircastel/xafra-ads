package com.commons.help.job.core;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;

public final class LogContent implements Serializable {
  private static final long serialVersionUID = 6957357279599587566L;
  
  private StringBuilder logBuilder = null;
  
  private String separator;
  
  public static class Builder implements Serializable {
    private static final long serialVersionUID = 5399490172084987795L;
    
    private StringBuilder logBuilder;
    
    private String separator;
    
    private boolean isEmpty = true;
    
    private boolean useAreaSepatator = false;
    
    private String areaSeparator = "\n------------------------------------------------------------";
    
    public Builder(String sourceContent) {
      this(sourceContent, "|");
    }
    
    public Builder(String sourceContent, String separator) {
      this.logBuilder = new StringBuilder(sourceContent + " ");
      this.separator = " " + separator + " ";
      this.logBuilder.append(this.separator);
    }
    
    public Builder add(Exception e) {
      return addError(e);
    }
    
    public Builder addError(Throwable e) {
      add(LogContent.throwableToString(e));
      return this;
    }
    
    public Builder areaSeparator(String areaSeparator) {
      this.areaSeparator = areaSeparator;
      return this;
    }
    
    public Builder useAreaSepator() {
      this.useAreaSepatator = true;
      return this;
    }
    
    public Builder addError(String logPart, Exception e) {
      add(logPart);
      add(LogContent.throwableToString(e));
      return this;
    }
    
    public Builder add(String logPart) {
      this.isEmpty = false;
      this.logBuilder.append(logPart).append(this.separator);
      return this;
    }
    
    public Builder addError(String logPart) {
      return add("[ERROR]" + logPart);
    }
    
    public Builder addWithFormat(String logPart, Object... varargs) {
      this.isEmpty = false;
      this.logBuilder.append(String.format(logPart, varargs)).append(this.separator);
      return this;
    }
    
    public Builder addErrorWithFormat(String logPart, Object... varargs) {
      return addWithFormat("[ERROR]" + logPart, varargs);
    }
    
    public boolean isEmpty() {
      return this.isEmpty;
    }
    
    public String toString() {
      return build();
    }
    
    public String build() {
      String content = (new LogContent(this)).getContent();
      if (this.useAreaSepatator)
        content = this.areaSeparator + content + this.areaSeparator; 
      return content;
    }
  }
  
  private LogContent(Builder builder) {
    this.logBuilder = builder.logBuilder;
    this.separator = builder.separator;
  }
  
  public StringBuilder getLogBuilder() {
    return this.logBuilder;
  }
  
  private String getContent() {
    String content = this.logBuilder.toString();
    return (!content.isEmpty() && content.length() >= this.separator.length()) ? content.substring(0, content.length() - this.separator.length()) : "";
  }
  
  public static String throwableToString(Throwable t) {
    StringWriter sWriter = new StringWriter();
    PrintWriter pWriter = new PrintWriter(sWriter);
    t.printStackTrace(pWriter);
    return sWriter.toString();
  }
}