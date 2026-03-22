# Decorator_Pattern# JCompress

Java command-line tool สำหรับ compress / encrypt และคำนวณ checksum ของไฟล์  
สร้างเพื่อ demonstrate การใช้ **Decorator Pattern** ร่วมกับ **Factory Pattern**

---

## Prerequisites

- **Java 8+** must be installed (`java -version` to check)
- No external libraries needed — uses only the Java standard library

---

## Quick Start

### 1. Compile

```bash
javac -d out src/*.java src/factory/*.java src/processor/*.java src/processor/impl/*.java
```

> `src/**/*.java` does **not** work reliably in Git Bash on Windows — list each folder explicitly instead.

### 2. Run

```bash
java -cp out JCompress <file> [options...]
```

All options are **optional** and can be combined freely.

---

## Usage

```bash
java -cp out JCompress <file> [-zip|-gzip] [-DES|-AES] [-MD5|-SHA256]
```

| Argument | Required | Description |
|---|---|---|
| `<file>` | Yes | Path to the input file |
| `-zip` / `-gzip` | No | Compression algorithm (choose one) |
| `-DES` / `-AES` | No | Encryption algorithm (choose one) |
| `-MD5` / `-SHA256` | No | Checksum algorithm (choose one) |

The output file is saved as `<file>.jc` in the same directory.

---

## Examples

**Compress + Encrypt + Checksum:**
```bash
java -cp out JCompress a.txt -zip -DES -MD5
```
```
=== JCompress ===
Input      : a.txt (1024 bytes)
Compression: ZIP
Encryption : DES
Checksum   : MD5

Processing chain:
  [MD5]    d41d8cd98f00b204e9800998ecf8427e
  [ZIP]    1024 B → 612 B
  [DES]    612 B → 616 B (encrypted)
  [FileWriter] wrote 616 bytes → a.txt.jc

Output: a.txt.jc
```

**GZIP + AES + SHA256:**
```bash
java -cp out JCompress a.txt -gzip -AES -SHA256
```

**Checksum only (no compression or encryption):**
```bash
java -cp out JCompress a.txt -MD5
```

**Compress only:**
```bash
java -cp out JCompress a.txt -zip
```

---

## Design Patterns

### 1. Decorator Pattern (หลัก)


```
Md5Decorator( ZipDecorator( DesDecorator( FileWriteProcessor ) ) )
         ↑             ↑           ↑               ↑
      checksum      compress    encrypt           write
     (outermost)                             (ConcreteComponent)
```

**Class hierarchy:**

| Slide (SalesTicket example) | JCompress |
|---|---|
| `Component` (abstract) | `Processor` (abstract) |
| `SalesTicket` | `FileWriteProcessor` |
| `TicketDecorator` (abstract) | `ProcessorDecorator` (abstract) |
| `Header1`, `Footer1` | `ZipDecorator`, `DesDecorator`, `Md5Decorator`, ... |
| `myTrailer` field | `myTrailer` field (same name) |
| `callTrailer()` | `callTrailer()` (same name) |

**ทำไมถึงเลือก Decorator?**  
ถ้า implement ด้วย inheritance แทน ต้องสร้าง subclass สำหรับทุก combination:  
`ZipDes`, `ZipAes`, `GzipDes`, `GzipAesMd5`, ... → explodes!  
Decorator แก้ปัญหานี้โดยให้ผู้ใช้เลือก combination ได้เองตอน runtime

### 2. Factory Pattern (รอง)

`ProcessorFactory.build()` รับ CLI args แล้วสร้าง decorator chain ที่ถูกต้อง  
ทำให้ `main()` ไม่ต้องรู้จัก concrete class เลย

```java
// Inside ProcessorFactory.build()
Processor chain = new FileWriteProcessor(outputPath);  // innermost first
chain = new DesDecorator(chain);                        // wrap encryption
chain = new ZipDecorator(chain);                        // wrap compression  
chain = new Md5Decorator(chain);                        // wrap checksum (outermost)
return chain;
```

### Java Stream as Decorator

Java I/O stream เองก็ implement Decorator Pattern อยู่แล้ว (`FilterOutputStream` = Decorator):

```java
// Inside ZipDecorator.execute():
DeflaterOutputStream zipStream = new DeflaterOutputStream(buffer);
// DeflaterOutputStream ห่อ OutputStream อีกที → Decorator ซ้อน Decorator
```

| Java Stream Class | Role in Decorator Pattern |
|---|---|
| `OutputStream` | Component |
| `FileOutputStream` | ConcreteComponent |
| `FilterOutputStream` | Decorator (abstract) |
| `DeflaterOutputStream` | ConcreteDecoratorA (compression) |
| `CipherOutputStream` | ConcreteDecoratorB (encryption) |

---

## Project Structure

```
src/
├── JCompress.java                   ← Entry point, parse CLI args
├── factory/
│   └── ProcessorFactory.java        ← Factory: builds decorator chain
└── processor/
    ├── Processor.java               ← Abstract Component
    ├── FileWriteProcessor.java      ← ConcreteComponent (writes to disk)
    ├── ProcessorDecorator.java      ← Abstract Decorator (myTrailer + callTrailer)
    └── impl/
        └── Decorators.java          ← All concrete decorators:
                                        ZipDecorator, GzipDecorator,
                                        DesDecorator, AesDecorator,
                                        Md5Decorator, Sha256Decorator
```

---

## Compile & Run

```bash
# Compile
javac -d out src/*.java src/factory/*.java src/processor/*.java src/processor/impl/*.java

# Run — full pipeline
java -cp out JCompress myfile.txt -zip -DES -MD5

# Run — different algorithms
java -cp out JCompress myfile.txt -gzip -AES -SHA256

# Run — checksum only (no compression or encryption)
java -cp out JCompress myfile.txt -MD5
```

---

## Supported Options

| Flag | Type | Description |
|---|---|---|
| `-zip` | Compression | ZLIB/Deflate compression |
| `-gzip` | Compression | GZIP compression |
| `-DES` | Encryption | DES (56-bit key) |
| `-AES` | Encryption | AES-128 |
| `-MD5` | Checksum | MD5 hash |
| `-SHA256` | Checksum | SHA-256 hash |

All flags are optional and can be combined freely.

---

## How `callTrailer()` Works (ตัวอย่าง)

เมื่อรัน `jcompress a.txt -zip -DES -MD5` chain ที่ได้คือ:

```
Md5Decorator
  └─ myTrailer: ZipDecorator
       └─ myTrailer: DesDecorator
            └─ myTrailer: FileWriteProcessor("a.txt.jc")
```

เมื่อ call `processor.execute(data)`:

```
Md5Decorator.execute(data)
  → คำนวณ MD5 hash แล้ว print
  → callTrailer(data)          ← ส่งต่อ data (ยังไม่แปลง) ไปให้ ZipDecorator
    → ZipDecorator.execute(data)
        → compress ด้วย Java DeflaterOutputStream
        → callTrailer(compressed)  ← ส่ง compressed bytes ต่อไป
          → DesDecorator.execute(compressed)
              → encrypt ด้วย Java CipherOutputStream
              → callTrailer(encrypted)
                → FileWriteProcessor.execute(encrypted)
                    → เขียนลงไฟล์ ✓
```


---

*CN332 Object-Oriented Analysis and Design — Thammasat University*