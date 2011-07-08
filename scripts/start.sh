. ./var.sh

busybox clear

busybox mkdir -p $mnt

busybox mount -o loop,noatime $kit/debian.img $mnt

busybox mount -t devpts -o mode=777,gid=0 devpts $mnt/dev/pts
busybox mount -t proc proc $mnt/proc
busybox mount -t sysfs sysfs $mnt/sys

busybox mkdir -p $mnt/mnt
busybox mkdir -p $mnt/mnt/sdcard $mnt/mnt/system
busybox mkdir -p $mnt/mnt/data $mnt/mnt/dev $mnt/mnt/external $mnt/mnt/root

busybox mount -o bind /sdcard $mnt/mnt/sdcard
busybox mount -o bind /sdcard/external_sd $mnt/mnt/external
busybox mount -o bind /system $mnt/mnt/system
busybox mount -o bind /data $mnt/mnt/data
busybox mount -o bind /dev $mnt/mnt/dev
busybox mount -t tmpfs tmpfs $mnt/tmp -o noatime,mode=1777
