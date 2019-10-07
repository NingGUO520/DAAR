#!/bin/bash
awk '{if ($3<1500) {
         print $3
     }
 }' rollernet.dyn/data
