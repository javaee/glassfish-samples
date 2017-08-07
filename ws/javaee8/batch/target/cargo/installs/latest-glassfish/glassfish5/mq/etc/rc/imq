#!/sbin/sh
#
# DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
#
# Copyright (c) 2000-2010 Oracle and/or its affiliates. All rights reserved.
#
# The contents of this file are subject to the terms of either the GNU
# General Public License Version 2 only ("GPL") or the Common Development
# and Distribution License("CDDL") (collectively, the "License").  You
# may not use this file except in compliance with the License.  You can
# obtain a copy of the License at
# https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
# or packager/legal/LICENSE.txt.  See the License for the specific
# language governing permissions and limitations under the License.
#
# When distributing the software, include this License Header Notice in each
# file and include the License file at packager/legal/LICENSE.txt.
#
# GPL Classpath Exception:
# Oracle designates this particular file as subject to the "Classpath"
# exception as provided by Oracle in the GPL Version 2 section of the License
# file that accompanied this code.
#
# Modifications:
# If applicable, add the following below the License Header, with the fields
# enclosed by brackets [] replaced by your own identifying information:
# "Portions Copyright [year] [name of copyright owner]"
#
# Contributor(s):
# If you wish your version of this file to be governed by only the CDDL or
# only the GPL Version 2, indicate your decision by adding "[Contributor]
# elects to include this software in this distribution under the [CDDL or GPL
# Version 2] license."  If you don't indicate a single choice of license, a
# recipient has the option to distribute your version of this file under
# either the CDDL, the GPL Version 2 or to extend the choice of license to
# its licensees as provided above.  However, if you add GPL Version 2 code
# and therefore, elected the GPL Version 2 license, then the option applies
# only if the new code is made subject to such option by the copyright
# holder.
#

#
# Message Queue Broker init.d script
#
# This script starts/stops the Message Queue broker process as part of 
# normal UNIX system operation based on the value of the
# AUTOSTART in the /etc/imq/imqbrokerd.conf file.
#
# To debug this script, set the environmental variable
# DEBUG to 1 (true)
#
# e.g.
# env DEBUG=1 /etc/init.d/imq start
#
# This file is installed as:
#     /etc/init.d/imq
#
# It is linked into the rc*.d directories as:
#	# ln imq /etc/rc3.d/S52imq
#	# ln imq /etc/rc0.d/K15imq
#	# ln imq /etc/rc1.d/K15imq
#	# ln imq /etc/rc2.d/K15imq
#
# The S52imq link in /etc/rc3.d will start the broker when
# the system enters multiuser mode. The K15imq links in
# rc0.d, rc1.d and rc2.d will bring down the broker when the
# system is brought down to single user, or administrative modes.
#
# The script looks at the file:  /etc/imq/imqbrokerd.conf to
# determine whether or not the broker should be started.

###### START LOCAL CONFIGURATION
# You may wish to modify these variables to suit your local configuration

# IMQ_HOME   Location of the Message Queue installation
IMQ_HOME=/
export IMQ_HOME

# IMQ_VARHOME
# Set IMQ_VARHOME if you wish to designate an alternate
# location for storing broker specific data like:
#	- persistent storage
#	- SSL certificates
#	- user passwd database
#
# IMQ_VARHOME=/var/opt/SUNWjmq
# export IMQ_VARHOME

# BROKER_OPTIONS	Default Options to pass to the broker executable
# additional arguments can be defined in the ARGS property
#
BROKER_OPTIONS="-silent"

#
# ETC HOME
IMQ_ETCHOME="/etc/imq"
#
# imqbrokerd.conf
CONF_FILE=$IMQ_ETCHOME/imqbrokerd.conf
#
#
###### END LOCAL CONFIGURATION

EXECUTABLE=imqbrokerd


# error "description"
error () {
  echo $0: $* 2>&1
  exit 1
}

# find the named process(es)
findproc() {
  pid=`/usr/bin/ps -ef |
  /usr/bin/grep -w $1 | 
  /usr/bin/grep -v grep |
  /usr/bin/awk '{print $2}'`
  echo $pid
}

# kill the named process(es) (borrowed from S15nfs.server)
killproc() {
   pid=`findproc $1`
   [ "$pid" != "" ] && kill $pid
}

#
# require /usr/bin and Message Queue
#
if [ ! -d /usr/bin ]; then
   error "Cannot find /usr/bin."

elif [ ! -d "$IMQ_HOME" ]; then
   error "Cannot find Message Queue in IMQ_HOME ($IMQ_HOME)."
fi




#
# Start/stop Message Queue Broker
#
case "$1" in
'start')
         if [ -f $CONF_FILE ] ; then 
            AUTOSTART=`grep -v "^#" $CONF_FILE | grep AUTOSTART | cut -c11-`
            RESTART=`grep -v "^#" $CONF_FILE | grep RESTART | cut -c9-`
            ARGS=`grep -v "^#" $CONF_FILE | grep ARGS | cut -c6-`
            if [ $AUTOSTART ] ; then 
                if [ $AUTOSTART = "YES" -o $AUTOSTART = "Yes" -o $AUTOSTART = "yes" -o $AUTOSTART = "Y" -o $AUTOSTART = "y" ] ; then
                    START=1
                    REASON="The AUTOSTART property is turned on (set to $AUTOSTART) in  $CONF_FILE. The ARGS property is set to \"$ARGS\""

                elif [ $AUTOSTART = "NO" -o $AUTOSTART = "No" -o $AUTOSTART = "no" -o $AUTOSTART = "N" -o $AUTOSTART = "n" ] ; then
                    START=0
                    REASON="The AUTOSTART property is turned off (set to $AUTOSTART) in $CONF_FILE"
                else
                    START=0
                    REASON="The AUTOSTART property is not set to an expected value (value is $AUTOSTART)"
                fi
            else 
                START=0
                REASON="AUTOSTART was not set in $CONF_FILE"
            fi
         else
            START=0
            REASON="$CONF_FILE, the configuration file, was not found"
         fi

         # Handle restart option
         if [ ! -z "$RESTART" ]; then
             if [ $RESTART = "YES" ]; then
                BROKER_OPTIONS="-autorestart $BROKER_OPTIONS"
             fi
         fi

         if [ $START -eq 0 ] ; then
             if [ $DEBUG ] ; then
                 echo $IMQ_HOME/bin/$EXECUTABLE is not being started because [ $REASON ]
             fi
             exit 0
         else
             if [ $DEBUG ] ; then
                 echo $IMQ_HOME/bin/$EXECUTABLE is starting because [ $REASON ]
             fi
         fi

         if [ $DEBUG ] ; then 
	     echo "Command \c"
	     echo "$IMQ_HOME/bin/$EXECUTABLE -bgnd $BROKER_OPTIONS  $ARGS... \c"
         fi
	 cd $IMQ_HOME

	 # Check if the server is already running.
	 if [ -n "`findproc $EXECUTABLE`" ]; then
             if [ $DEBUG ] ; then 
	        echo "$EXECUTABLE is already running."
             fi
	     exit 0
	 fi

	 bin/$EXECUTABLE -bgnd $BROKER_OPTIONS $ARGS &
         if [ $DEBUG ] ; then 
  	     echo "done"
         fi

	;;

'stop')
        if [ $DEBUG ] ; then
	    echo "Stopping $EXECUTABLE ... \c"
        fi
	if [ -z "`findproc $EXECUTABLE`" ]; then
            if [ $DEBUG ] ; then
	        echo "$EXECUTABLE is not running."
            fi
	   exit 0
	fi
	killproc $EXECUTABLE
        if [ $DEBUG ] ; then
	    echo "done"
        fi
	;;

*)
	echo "Usage: $0 { start | stop }"
	;;
esac
