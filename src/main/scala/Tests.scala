import org.scalacheck.Arbitrary
import org.scalacheck.Prop.forAll
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.prop.Configuration
import org.typelevel.discipline.Laws
import org.typelevel.discipline.scalatest.FunSuiteDiscipline

import java.nio.ByteBuffer

object Tests {
  trait ByteDecoder[A] {
    def decode(bytes: Array[Byte]): Option[A]
  }

  trait BYteEncoder[A] {
    def encode(a: A): Array[Byte]
  }

  trait ByteCodec[A] extends BYteEncoder[A] with ByteDecoder[A]

  trait ByteCodecLaws[A] {
    def codec: ByteCodec[A]

    def isomorphism(a: A): Boolean = codec.decode(codec.encode(a)) == Some(a)
  }

  trait ByteCodecTests[A] extends Laws {
    def laws: ByteCodecLaws[A]

    def byteCodec(implicit arb: Arbitrary[A]): RuleSet = new DefaultRuleSet(
      name = "byteCodec",
      parent = None,
      "isomorphism" -> forAll(laws.isomorphism _)
    )
  }

  object IntByteCodec extends ByteCodec[Int] {
    override def decode(bytes: Array[Byte]): Option[Int] = {
      if (bytes.length !=4) None
      else {
        val bb = ByteBuffer.allocate(4)
        bb.put(bytes)
        bb.flip()
        Some(bb.getInt)
      }
    }

    override def encode(a: Int): Array[Byte] = {
      val bb = ByteBuffer.allocate(4)
      bb.putInt(a)
      bb.array()
    }
  }

  object IntByteCodeLaws extends ByteCodecLaws[Int] {
    override def codec: ByteCodec[Int] = IntByteCodec
  }

  object IntByteCodecTests extends ByteCodecTests[Int] {
    override def laws: ByteCodecLaws[Int] = IntByteCodeLaws
  }

}

import Tests._

class ByteCodeSpec extends AnyFunSuite with Configuration with FunSuiteDiscipline {
  checkAll("ByteCodec[Int]", IntByteCodecTests.byteCodec)
}