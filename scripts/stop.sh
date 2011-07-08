. ./var.sh

busybox umount -l $mnt/sys
busybox umount -l $mnt/proc
busybox umount -l $mnt/dev/pts
busybox umount -l $mnt/mnt/external
busybox umount -l $mnt/mnt/sdcard
busybox umount -l $mnt/mnt/system
busybox umount -l $mnt/mnt/data
busybox umount -l $mnt/tmp
busybox umount -ld $mnt/
