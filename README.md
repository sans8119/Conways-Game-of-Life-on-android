# Conways-Game-of-Life-on-android
Conways Game of Life implemented on Android 


1) Conways game of life has been coded in android; 
2) The algorithmic implementation has been largely borowed from http://www.drdobbs.com/jvm/an-algorithm-for-compressing-space-and-t/184406478 with certain modifications and enhancements. It has been enhanced to use lessor memory.
3) As of now 20 x 20 grid is being used to show a part of the universe. I am figuring out a way to show the entire univers when it grows to trillions of cells. 
4) The algorithm grows one step at a time. Time to find the next generation is found to be around 1 ms. Only 5 generatons are being shown in a second so that user can have a better feel of what is going on. 
5) Using Hashlife algorithm grows the universe by many steps at a time. Hence using this algorithm was leading to outofmemory errors very soon. In the future plans are to use B+ tree for large quadtree to store such a huge universes. 

