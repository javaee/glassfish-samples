#!/bin/sh
#
# DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
#
# Copyright (c) 2000-2012 Oracle and/or its affiliates. All rights reserved.
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
# Message Queue Database Administration startup script
#
# Script specific properties:
#   -javahome <path>	Use <path> as the location of the Java runtime
#

# Specify additional arguments to the JVM here
_def_jvm_args="-Xmx128m"

_bin_home=`/usr/bin/dirname $0`
_init_file="imqinit"

# Source initialization file. This intitializes the imq_* variables
if [ -f $_bin_home/../share/lib/imq/$_init_file ]; then
    # bundled location
    . $_bin_home/../share/lib/imq/$_init_file
elif [ -f $_bin_home/../lib/$_init_file ]; then
    # unbundled location
    . $_bin_home/../lib/$_init_file
elif [ -f $_bin_home/../private/share/lib/$_init_file ]; then
    # Linux "standard" location
    . $_bin_home/../private/share/lib/$_init_file
#####hpux-dev#####
elif [ -f $_bin_home/../private/share/lib/$_init_file ]; then
    # HP-UX "standard" location
    $_bin_home/../private/share/lib/$_init_file
else
    echo "Error: Could not find required Message Queue initialization file '$_init_file'"
    exit 1
fi

# Parse command line arguments. We eat these so they are not passed to dbmgr
while [ $# != 0 ]; do
  case "$1" in
    -verbose) _verbose=true; shift 1;;
    -javahome) shift 2;;
    -vmargs) shift; _jvm_args="$1 $_jvm_args"; shift ;;
    *)  args="$args $1"; shift  ;;
  esac
done

# classes needed by imqdbmgr
_classes=$imq_sharelibimq_home/imqbroker.jar

# Default external JARs
if [ ! -z "$imq_ext_jars" ]; then
    _classes=$_classes:$imq_ext_jars
fi

# Additional classes possibly needed for JDBC provider
_classes=$_classes:$imq_sharelibimq_home/ext
# Put all jar and zip files in $imq_varhome/lib in the classpath
for file in $imq_sharelibimq_home/ext/*.jar $imq_sharelibimq_home/ext/*.zip; do
    if [ -r "$file" ]; then
	_classes=$_classes:$file
    fi
done

#####hpux-dev#####
# On Linux and HP-UX they may be here (as of 3.6)
_classes=$_classes:$imq_sharelib_home/ext
# Put all jar files in $IMQ_SHARELIB_HOME/ext in our CLASSPATH
for _file in $imq_sharelib_home/ext/*.jar $imq_sharelib_home/ext/*.zip; do
    if [ -r "$_file" ]; then
        _classes=$_classes:$_file
    fi
done

# imqdbmgr's main class
_mainclass=com.sun.messaging.jmq.jmsserver.persist.jdbc.DBTool

# setup arguments to the JVM
jvm_args="$_def_jvm_args $_jvm_args -Dimq.home=$imq_home -Dimq.varhome=$imq_varhome -Dimq.libhome=$imq_sharelibimq_home -Dimq.etchome=$imq_etchome"

# Needed to locate libimq
#####hpux-dev#####
PLATFORM=`uname`
if [ "$PLATFORM" = HP-UX ] ; then
SHLIB_PATH=$SHLIB_PATH:$imq_libhome; export SHLIB_PATH
else
LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$imq_libhome; export LD_LIBRARY_PATH
fi

# run imqdbmgr
"$imq_javahome/bin/java" -cp $_classes $jvm_args $_mainclass $args

