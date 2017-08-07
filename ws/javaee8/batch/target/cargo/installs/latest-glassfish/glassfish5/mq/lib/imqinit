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
# Common initialization script used by all imq wrapper scripts.
# After this script runs the following local shell variables
# are defined (they are not exported):
#
# imq_javahome      Location of Java to use. Calling script must still check
#                   for -javahome command line argument which should override
#                   this value.
#
# imq_javareason    Where the value for javahome came from
#
# We have all of these variables because of the bundled Solaris installation
# which spreads Message Queue out across the disk:
# imq_home              Root of Message Queue installation
# imq_etchome           Root of etc subtree
# imq_libhome           Root of lib subtree
# imq_sharelib_home     Root of share/lib subtree
# imq_sharelibmq_home   Root of share/lib/mq subtree
# imq_varhome           Root of var subtree
#
# imq_servervm      "-server" If the server JVM is available, else ""
# imq_os            Value of `uname`. SunOS or Linux
# imq_osver         Value of `uname -r`.
# imq_proc          Processor type: i386 or sparc
#
# imq_ext_jars      External JARs to include in classpath
#
# imq_installtype   Destcription of installation type

#
# Check if a server version of the VM is available
#

check_for_servervm() {
PLATFORM=`uname`
    # If the server version of the VM is there use it
#####hpux-dev#####
if [ "$PLATFORM" = HP-UX ] ; then
    if [ -f $imq_javahome/jre/lib/PA_RISC2.0/server/libjvm.sl ] ; then
       imq_servervm=-server
    fi
else
    if [ -f $imq_javahome/jre/lib/"$imq_proc"/server/libjvm.so ] ||
       [ -f $imq_javahome/lib/"$imq_proc"/server/libjvm.so ]; then
	imq_servervm=-server
    fi
fi
}

#
# Search the OS for J2SE in the standard locations. This sets
# the default location for a platform.
#
#####hpux-dev#####
findj2se() {

    case "$imq_os" in
    SunOS)
        if [ "$imq_osver" = "5.9" -o "$imq_osver" = "5.8" -o "$imq_osver" = "5.7" ];  then
            _j2se_locations="\
/usr/jdk/latest \
/usr/jdk/jdk1.7.* \
"
        else
            # Solaris 10 or newer. 
            _j2se_locations="\
/usr/jdk/latest \
/usr/jdk/jdk1.7.* \
/usr/java \
/usr/jdk/entsys-j2se \
"
        fi
        ;;

    Linux)
        _j2se_locations="\
/usr/java/latest \
/usr/java/jre1.7.* \
/usr/java/jdk1.7.* \
"
        ;;

    HP-UX)
       _j2se_locations="\
/opt/java1.7* \
"
        ;;

    AIX)
       _j2se_locations="\
/usr/java7* \
"
        ;;

    Darwin)
            _j2se_locations="\
/Library/Java/JavaVirtualMachines/jdk1.7.* \
/Library/Java/Home \
/usr \
"
        ;;

    *)
        _j2se_locations="\
/usr/java \
/usr/java/jre1.7.* \
/usr/java/jdk1.7.* \
$mq_home/jre \
"
        ;;
    esac

    # Scan list and pick first J2SE we find. Note this does not check version
    for f in $_j2se_locations; do
        if [ -x $f/bin/java ] ; then
            imq_javareason="Default location: $_j2se_locations"
            imq_javahome=$f
            break
        fi
    done
}

_bin_home=`/usr/bin/dirname $0`

# Determine the processor type
case "`uname -m`" in
    i[3-6]86  | ia32 | ia64 | i*86*)
        imq_proc=i386
        ;;
    x86*64 | amd64)
        imq_proc=i386
        ;;
    sparc* | sun4*)
        imq_proc=sparc
        ;;
    *)
        if [ `uname` = "HP-UX" ] ; then
                imq_proc="`uname -m`"
        else
                imq_proc="`uname -p`"
        fi
        ;;
esac

# Determine the OS type
imq_os="`uname`"
imq_osver="`uname -r`"

# check if we were installed as part of a solaris package OR
# another (isa_eval, linux) type of installation
imq_installtype=""
if [ -f $_bin_home/../lib/imqbroker.jar ] ; then
# set default locations (not solaris package)
#
# first we have to determine if these are the new zip layout
#
    if [ -d $_bin_home/../../etc/mq ] ; then
#
# Yep - new location
#
        imq_installtype="Zip-based install"
        imq_home=$_bin_home/..
        imq_etchome=$imq_home/../etc/mq
        imq_libhome=$imq_home/lib
        imq_sharelib_home=$imq_libhome
        imq_sharelibimq_home=$imq_sharelib_home
        imq_varhome=$imq_home/../var/mq

    else
# Nope - use old locations
    
        if [ $imq_os = "Linux" ] ; then
            imq_installtype="Linux compatibility install"
    #####hpux-dev##### Bug Id 6256771
        elif [ $imq_os = "HP-UX" ] ; then
            imq_installtype="HP-UX install"
        else
            imq_installtype="Non-package install"
        fi
        imq_home=$_bin_home/..
        imq_etchome=$imq_home/etc
        imq_libhome=$imq_home/lib
        imq_sharelib_home=$imq_libhome
        imq_sharelibimq_home=$imq_sharelib_home
        imq_varhome=$imq_home/var
    fi
elif [ -f $_bin_home/../private/share/lib/imqbroker.jar ] ; then
#####hpux-dev#####
     if [ $imq_os = "HP-UX" ] ; then
         # New "standard" HP-UX install location
         imq_installtype="HP-UX standard install"
     else
         # New "standard" Linux install location
         imq_installtype="Linux standard install"
     fi
    imq_home=$_bin_home/..
# Use etc symlink
    imq_etchome=$imq_home/etc
    imq_libhome=$imq_home/lib
    imq_sharelib_home=$imq_home/share/lib
    imq_sharelibimq_home=$imq_home/private/share/lib
# Use var symlink
    imq_varhome=$imq_home/var
else
# solaris install
    imq_installtype="Solaris package install"
    imq_home=$_bin_home/../..
    imq_etchome=$imq_home/etc/imq
    imq_libhome=$imq_home/usr/lib
    imq_sharelib_home=$imq_home/usr/share/lib
    imq_sharelibimq_home=$imq_sharelib_home/imq
    imq_varhome=$imq_home/var/imq
fi

# Locate the default J2SE. This will set imq_javahome to it
findj2se;

# Next load what's in imqenv.conf file. In 3.6 imqenv.conf moved from
# from /etc/imq to /usr/share/lib/imq so we check both places.
_env_files="$imq_etchome/imqenv.conf $imq_sharelibimq_home/imqenv.conf"
for _f in $_env_files; do
    if [ -f "$_f" ] ; then
        if [ -r "$_f" ] ; then
            . "$_f"
            if [ -n "$IMQ_DEFAULT_JAVAHOME" ] ; then
                imq_javahome=$IMQ_DEFAULT_JAVAHOME
                imq_javareason="File $_f"
                IMQ_DEFAULT_JAVAHOME=
            fi
        else
            echo "Warning: Could not read $_f -- ignoring"
        fi
    fi
done
imq_varhome=${IMQ_DEFAULT_VARHOME:-$imq_varhome}
imq_etchome=${IMQ_DEFAULT_ETCHOME:-$imq_etchome}
imq_ext_jars=${IMQ_DEFAULT_EXT_JARS:-$imq_ext_jars}

# OK - see if the files exist, if not change to

# Override imq_varhome with the IMQ_VARHOME env variable
imq_varhome=${IMQ_VARHOME:-$imq_varhome}

# Override imq_varhome with -varhome
_varhomenext=false
for _opt in "$@"
do
  if [ $_varhomenext = true ]
  then
    imq_varhome=$_opt
    _varhomenext=false
  elif [ "$_opt" = "-varhome" ]
  then
    _varhomenext=true;
  fi
done

# Override imq_javahome with the IMQ_JAVAHOME env variable
if [ -n "$IMQ_JAVAHOME" ] ; then
    imq_javahome=${IMQ_JAVAHOME:-$imq_javahome}
    imq_javareason="IMQ_JAVAHOME"
fi

# Next override with what's in jdk.env
# jdk.env is deprecated. It has been replace by imqenv.conf
_jdk_env_file=$imq_varhome/jdk.env
if [ -f $_jdk_env_file ] ; then
    if [ -r $_jdk_env_file ] ; then
       _jdk_env_home=`/bin/cat $_jdk_env_file`
       imq_javahome=$_jdk_env_home
       imq_javareason="File $_jdk_env_file"
    else
        echo "Warning: Could not read $_jdk_env_file -- ignoring"
    fi
fi

# Finally check for -javahome. We do this in a fashion that does not
# disrupt the original argument list
_javahomenext=false
for _opt in "$@"
do
  if [ $_javahomenext = true ]
  then
    imq_javahome=$_opt
    imq_javareason="-javahome"
    _javahomenext=false
  fi
  if [ "$_opt" = "-javahome" ] || [ "$_opt" = "-jrehome" ]
  then
    _javahomenext=true;
  elif [ "$_opt" = "-verbose" ] 
  then
    _verbose=true;
  fi
done

if [ -z "$imq_javahome" ]; then
   imq_javahome=`which java | grep java | sed -e "s/bin\/java//"`	
   if [ -z "$imq_javahome" ] ; then
       imq_javahome = `which java | grep java`
       echo $imq_javahome
       echo "Error: Could not find a valid J2SE."
       echo "Searched: $_j2se_locations"
       echo "Use IMQ_JAVAHOME or -javahome to specify the location of an alternate J2SE."
       exit 1
   fi
fi

if [ ! -x "$imq_javahome/bin/java" ] 
then
   echo "Error: Invalid J2SE location: $imq_javahome"
   echo "J2SE location was specified using: $imq_javareason"
   echo "Exiting"
   exit 1
fi

case $imq_varhome in
\.*) imq_varhome=$_bin_home/${imq_varhome};;
esac
case $IMQ_VARHOME in
\.*) IMQ_VARHOME=$_bin_home/${IMQ_VARHOME};;
esac
case $imq_etchome in
\.*) imq_etchome=$_bin_home/${imq_etchome};;
esac
case $imq_ext_jars in
\.*) imq_ext_jars=$_bin_home/${imq_ext_jars};;
esac
case $imq_home in
\.*) imq_home=$_bin_home/${imq_home};;
esac


check_for_servervm;

if [ ! -z "$_verbose" ]; then
    echo ""
    echo "Starting $0"
    echo "Environment is:"
    echo "    j2se location                : $imq_javahome" 
    echo "    j2se specified               : $imq_javareason" 
    echo "    Message Queue install type   : $imq_installtype" 
    echo "    OS                           : $imq_os" 
    echo "    OS Version                   : $imq_osver" 
    echo "    Processor type               : $imq_proc" 
    echo "    Current Directory            : $_bin_home"
    echo "    IMQ_HOME                     : $imq_home" 
    echo "    IMQ_VARHOME                  : $IMQ_VARHOME" 
    echo "    imq_etchome                  : $imq_etchome" 
    echo "    imq_varhome                  : $imq_varhome" 
    echo "    imq_ext_jars                 : $imq_ext_jars" 
    echo "    Default J2SE locations       : $_j2se_locations" 
fi

