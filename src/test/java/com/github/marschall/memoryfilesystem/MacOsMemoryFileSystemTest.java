package com.github.marschall.memoryfilesystem;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class MacOsMemoryFileSystemTest {

  @Rule
  public final MacOsFileSystemRule rule = new MacOsFileSystemRule();

  private FileSystem fileSystem;

  private final boolean useDefault;

  public MacOsMemoryFileSystemTest(boolean useDefault) {
    this.useDefault = useDefault;
  }

  FileSystem getFileSystem() {
    if (this.fileSystem == null) {
      if (this.useDefault) {
        this.fileSystem = FileSystems.getDefault();
      } else {
        this.fileSystem = this.rule.getFileSystem();
      }
    }
    return this.fileSystem;
  }


  @Parameters
  public static List<Object[]> fileSystems() throws IOException {
    String osName = (String) System.getProperties().get("os.name");
    boolean isMac = osName.startsWith("Mac");
    if (isMac) {
      return Arrays.asList(new Object[]{true},
              new Object[]{false});
    } else {
      return Collections.singletonList(new Object[]{false});
    }
  }

  @Test
  public void macOsNormalization() throws IOException {
    FileSystem fileSystem = this.getFileSystem();
    String aUmlaut = "\u00C4";
    Path aPath = fileSystem.getPath(aUmlaut);
    String normalized = Normalizer.normalize(aUmlaut, Form.NFD);
    Path nPath = fileSystem.getPath(normalized);

    Path createdFile = null;
    try {
      // make sure parent exists
      Files.createDirectories(aPath.toAbsolutePath().getParent());
      createdFile = Files.createFile(aPath);
      assertEquals(1, createdFile.getFileName().toString().length());
      assertEquals(1, createdFile.toAbsolutePath().getFileName().toString().length());
      assertEquals(2, createdFile.toRealPath().getFileName().toString().length());

      assertTrue(Files.exists(aPath));
      assertTrue(Files.exists(nPath));
      assertTrue(Files.isSameFile(aPath, nPath));
      assertTrue(Files.isSameFile(nPath, aPath));
      assertThat(aPath, not(equalTo(nPath)));
    } finally {
      if (createdFile != null) {
        Files.delete(createdFile);
      }
    }

  }

  @Test
  public void macOsComparison() throws IOException {
    FileSystem fileSystem = this.getFileSystem();
    Path aLower = fileSystem.getPath("a");
    Path aUpper = fileSystem.getPath("A");
    assertThat(aLower, not(equalTo(aUpper)));
    Path createdFile = null;
    try {
      // make sure parent exists
      Files.createDirectories(aLower.toAbsolutePath().getParent());
      createdFile = Files.createFile(aLower);
      assertTrue(Files.exists(aLower));
      assertTrue(Files.exists(aUpper));
      assertTrue(Files.isSameFile(aLower, aUpper));
    } finally {
      if (createdFile != null) {
        Files.delete(createdFile);
      }
    }
  }

  @Test
  public void macOsPaths() throws IOException {
    FileSystem fileSystem = this.getFileSystem();
    String aUmlaut = "\u00C4";
    String normalized = Normalizer.normalize(aUmlaut, Form.NFD);
    assertEquals(1, aUmlaut.length());
    assertEquals(2, normalized.length());
    Path aPath = fileSystem.getPath("/" + aUmlaut);
    Path nPath = fileSystem.getPath("/" + normalized);
    assertEquals(1, aPath.getName(0).toString().length());
    assertThat(aPath, not(equalTo(nPath)));
  }

}