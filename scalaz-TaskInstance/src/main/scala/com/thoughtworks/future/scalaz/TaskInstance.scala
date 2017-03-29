package com.thoughtworks.future.scalaz

import com.thoughtworks.future.Continuation._
import com.thoughtworks.future.Future.Zip

import scala.util.{Failure, Success}
import scalaz.MonadError

/**
  * @author 杨博 (Yang Bo) &lt;pop.atry@gmail.com&gt;
  */
trait TaskInstance extends MonadError[Task, Throwable] with ContinuationInstance[Unit] with scalaz.Zip[Task] {

  override final def ap[A, B](fa: => Task[A])(f: => Task[A => B]) = {
    Zip(fa, f).map { pair: (A, A => B) =>
      pair._2(pair._1)
    }
  }

  override final def apply2[A, B, C](fa: => Task[A], fb: => Task[B])(f: (A, B) => C) = {
    Zip(fa, fb).map { pair =>
      f(pair._1, pair._2)
    }
  }

  override final def zip[A, B](a: => Task[A], b: => Task[B]): Task[(A, B)] = {
    Zip(a, b)
  }
}

object TaskInstance {

  implicit def scalazTaskInstance: TaskInstance = new TaskInstance {}

}
