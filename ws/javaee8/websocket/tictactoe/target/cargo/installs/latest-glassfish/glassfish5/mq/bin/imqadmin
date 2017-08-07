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
# JMS Administration Console startup script
#

_bin_home=`/usr/bin/dirname $0`
_init_file="imqinit"

# Source initialization file. This intitializes the imq_* variables
if [ -f $_bin_home/../share/lib/imq/$_init_file ]; then
    # Bundled location
    .   $_bin_home/../share/lib/imq/$_init_file
elif [ -f $_bin_home/../lib/$_init_file ]; then
    # Unbundled location
    . $_bin_home/../lib/$_init_file
elif [ -f $_bin_home/../private/share/lib/$_init_file ]; then
    # Linux "standard" location
    . $_bin_home/../private/share/lib/$_init_file
#####hpux-dev#####
elif [ -f $_bin_home/../private/share/lib/$_init_file ];then
    # HP-UX "standard" location
    . $_bin_home/../private/share/lib/$_init_file
else
    echo "Error: Could not find required Message Queue initialization file '$_init_file'"
    exit 1
fi

#location of the java help jar on solaris 9
helpjar_location=/usr/j2se/opt/javahelp/lib/jhall.jar

#location of the java help jar on linux
# We get it for free in the jdk only if the JDK was installed first.
helpjar_location_linux1=/usr/java/javahelp2.0/javahelp/lib/jhall.jar
helpjar_location_linux2=/usr/java/packages/javax.help-2.0/javahelp/lib/jhall.jar

#####hpux-dev#####
helpjar_location_HPUX=/opt/sun/share/lib/javahelpruntime/javahelp/lib/jhall.jar

# Specify additional arguments to the JVM here
jvm_args="-Xmx128m"

jvm_args="$jvm_args -Dimq.home=$imq_home -Dimq.varhome=$imq_varhome -Dimq.libhome=$imq_sharelibimq_home"

helpjar=$imq_sharelibimq_home/jhall.jar
if [ -f $helpjar_location ]; then
    helpjar=$helpjar_location;
elif [ -f $helpjar_location_linux1 ]; then
    helpjar=$helpjar_location_linux1;
elif [ -f $helpjar_location_linux2 ]; then
    helpjar=$helpjar_location_linux2;
#####hpux-dev#####
elif [ -f $helpjar_location_HPUX ]; then
    helpjar=$helpjar_location_HPUX;
fi

#
# Append CLASSPATH value to _classes if it is set.
#
if [ ! -z "$CLASSPATH" ]; then
    _classes=$imq_sharelibimq_home/imqadmin.jar:$imq_sharelib_home/fscontext.jar:$helpjar:$imq_sharelibimq_home/help:$CLASSPATH
    CLASSPATH=
    export CLASSPATH
else
    _classes=$imq_sharelibimq_home/imqadmin.jar:$imq_sharelib_home/fscontext.jar:$helpjar:$imq_sharelibimq_home/help
fi

# Default external JARs
if [ ! -z "$imq_ext_jars" ]; then
    _classes=$_classes:$imq_ext_jars
fi

_mainclass=com.sun.messaging.jmq.admin.apps.console.AdminConsole

"$imq_javahome/bin/java" -cp "$_classes" $jvm_args $_mainclass "$@"
