package com.google.code.memoryfilesystem;

import java.io.IOException;
import java.net.URI;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchEvent.Modifier;
import java.util.List;

final class AbsolutePath extends ElementPath {
  
  private final Path root;

  AbsolutePath(MemoryFileSystem fileSystem, Path root, List<String> nameElements) {
    super(fileSystem, nameElements);
    this.root = root;
  }

  @Override
  public boolean isAbsolute() {
    return true;
  }

  @Override
  public Path getRoot() {
    return this.root;
  }
  
  @Override
  public String toString() {
    StringBuilder buffer = new StringBuilder();
    buffer.append(this.root);
    boolean first = true;
    String separator = this.getFileSystem().getSeparator();
    for (String element : this.getNameElements()) {
      if (!first) {
        buffer.append(separator);
      }
      buffer.append(element);
      first = false;
    }
    return buffer.toString();
  }

  @Override
  public Path getParent() {
    if (this.getNameElements().size() == 1) {
      return this.root;
    } else {
      List<String> subList = this.getNameElements().subList(0, this.getNameElements().size() - 1);
      return new AbsolutePath(getMemoryFileSystem(), this.root, subList);
    }
  }


  @Override
  public Path getName(int index) {
    if (index < 0) {
      throw new IllegalArgumentException("index must be positive but was " + index);
    }
    if (index >= this.getNameCount()) {
      throw new IllegalArgumentException("index must not be bigger than " + (this.getNameCount() - 1) +  " but was " + index);
    }
    List<String> subList = this.getNameElements().subList(0, index + 1);
    return new AbsolutePath(getMemoryFileSystem(), this.root, subList);
  }


  @Override
  public Path subpath(int beginIndex, int endIndex) {
    // TODO Auto-generated function stub
    return null;
  }


  @Override
  public Path normalize() {
    // TODO Auto-generated function stub
    return null;
  }


  @Override
  public Path resolve(String other) {
    // TODO Auto-generated function stub
    return null;
  }


  @Override
  public Path resolveSibling(String other) {
    // TODO Auto-generated function stub
    return null;
  }


  @Override
  public URI toUri() {
    // TODO Auto-generated function stub
    return null;
  }


  @Override
  public Path toAbsolutePath() {
    return this;
  }


  @Override
  public Path toRealPath(LinkOption... options) throws IOException {
    // TODO Auto-generated function stub
    return null;
  }


  @Override
  public WatchKey register(WatchService watcher, Kind<?>[] events,
          Modifier... modifiers) throws IOException {
    // TODO report bug
    // TODO Auto-generated function stub
    return null;
  }


  @Override
  public WatchKey register(WatchService watcher, Kind<?>... events)
          throws IOException {
    // TODO Auto-generated function stub
    return null;
  }


  @Override
  public int compareTo(Path other) {
    // TODO Auto-generated function stub
    return 0;
  }


  @Override
  boolean startsWith(AbstractPath other) {
    // TODO Auto-generated function stub
    return false;
  }


  @Override
  boolean endsWith(AbstractPath other) {
    // TODO Auto-generated function stub
    return false;
  }


  @Override
  Path resolve(AbstractPath other) {
    // TODO Auto-generated function stub
    return null;
  }


  @Override
  Path resolveSibling(AbstractPath other) {
    // TODO Auto-generated function stub
    return null;
  }


  @Override
  Path relativize(AbstractPath other) {
    // TODO Auto-generated function stub
    return null;
  }
  

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof AbsolutePath)) {
      return false;
    }
    AbsolutePath other = (AbsolutePath) obj;
    return this.root.equals(other.root)
            && this.getNameElements().equals(other.getNameElements());
  }
  

  @Override
  public int hashCode() {
    return this.root.hashCode() ^ this.getNameElements().hashCode();
  }

}