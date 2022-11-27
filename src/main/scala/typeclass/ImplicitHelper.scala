package typeclass

import java.io.FileOutputStream
import java.nio.ByteBuffer
import scala.util.Using

trait Encoder[A] {
  def encode(a: A): Array[Byte]
}

object Encoder {

  implicit object IntByteEncoder extends Encoder[Int] {
    override def encode(a: Int): Array[Byte] = {
      val bb = ByteBuffer.allocate(4)
      bb.putInt(a)
      bb.array()
    }
  }

  implicit object StringByteEncoder extends Encoder[String] {
    override def encode(a: String): Array[Byte] = {
      a.getBytes()
    }
  }

}

trait OutputChannel {
  def write[A](obj: A)(implicit enc: Encoder[A]): Unit
}

object FileOutputChannel extends OutputChannel {
  def write[A](obj: A)(implicit enc: Encoder[A]): Unit = {
    val bytes = enc.encode(obj)
    Using(new FileOutputStream("/scala-functional-programming/test")) { os =>
      os.write(bytes)
      os.flush()
    }
  }
}

object Helper extends App {
  FileOutputChannel.write(1)
}