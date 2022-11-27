package typeclass

import java.io.FileOutputStream
import java.nio.ByteBuffer
import scala.util.Using

trait ByteEncoder[A] {
  def encode(a: A): Array[Byte]
}

trait Channel {
  def write[A](obj: A, enc: Encoder[A]): Unit
}

object FileChannel extends Channel {
  def write[A](obj: A, enc: Encoder[A]): Unit = {
    val bytes = enc.encode(obj)
    Using(new FileOutputStream("/scala-functional-programming/test")) { os =>
      os.write(bytes)
      os.flush()
    }
  }
}

object IntByteEncoder extends Encoder[Int] {
  override def encode(a: Int): Array[Byte] = {
    val bb = ByteBuffer.allocate(4)
    bb.putInt(a)
    bb.array()
  }
}

object StringByteEncoder extends Encoder[String] {
  override def encode(a: String): Array[Byte] = {
    a.getBytes()
  }
}

object Test extends App {
  FileChannel.write(1, IntByteEncoder)
}