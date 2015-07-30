# OddEvenPrinter
Cylic barrier and atomic integer taken from - http://rohandhapodkar.blogspot.in/2012/04/print-odd-and-even-numbers-using-two.html.

Cyclic barrier needs 2 barrier - Odd Thread prints between one barrier and even thread between another barrier.

Problem with above approach is for printing one odd even number pair using 2 threads, Threads will be blocked twice and which will involve thread context switches.

Implementation of java.util.concurrent.ConcurrentLinkedQueue helped me to think this problem as Non Blocking problem. Just to give you background about ConcurrentLinkedQueue implementation, it's not using any of the plain old wait/notify mechanism or java.util.concurrent.Lock which is introduced in Java 5. Both these mechanisms will park your thread if monitor is already acquired by another thread or Lock is acquired by another thread (tryLock() will not block your thread).

Coming back to our ConcurrentLinkedQueue implementation, it internally using java.util.concurrent.atomic.AtomicReferenceFieldUpdater to update head and tail node references. Instead of every thread competing to enter into critical section (which consist of updating head/tail pointer for poll/offer operation) and blocking if lock is acquired by another thread, every thread will try to update head/tail reference from it's current reference to new reference. AtomicReferenceFieldUpdater guarantees that only one thread can succeed and other threads will fail. other failed threads will keep on updating references until they succeed.


Odd Thread can acquire lock only when current state is 0. Once it will acquire lock (at line 29), it will update current state to 1 (ie. Odd thread is holding lock). Once odd thread holds lock, it prints odd value and then release lock by setting AtomicInteger value to 2( ie. Lock is released by Odd Thread. at line 34).

While Odd thread is holding lock, even thread also tries to update value from 2 to 3 in infinite loop( at line 52). This operation will succeed only after Odd Thread updates value from 1 to 2(at line 34). Once Even thread done with it's turn, it updates value from 3 to 0 (at line 57). 

Time required using AtomicInteger is definitely less than time it took using Cyclicbarrier.

Advantage of using AtomicInteger here:

This solution can be extended for many threads.

It also has some limitations as mentioned below:

make sure critical section needs to be really short. Runs in few CPU cycles. because other threads will be also competing for lock using busy waiting approach.
Any blocking operations like I/O, wait/notify should not be executed from Critical section.
