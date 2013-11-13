/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2011 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

/**
 * Server Sent Event message handling function dynamically builds a table
 * based on the message data.
 */ 
function stockHandler(event) {
    var eventData = event.data.toString().replace(/^\s*/,"").replace(/\s*$/,"");
    var stockTable = document.getElementById("stockTable");
    var rowCount = stockTable.rows.length - 1;
    if (eventData.length == 0) {
        stockTable.style.visibility="hidden";
        clearTableRows(stockTable, rowCount);
        return;
    }
    var quotes = eventData.split(" ");
    if (quotes.length !== rowCount || quotes.length == 0) {
        clearTableRows(stockTable, rowCount);
    }
    if (quotes !== null) {
        stockTable.style.visibility="visible";
        var newRow, newCell;
        for (var i = 0; i < quotes.length; i++) {
            var fields = quotes[i].split(":");
            if (document.getElementById(fields[0]) !== 'undefined' &&
                document.getElementById(fields[0]) !== null) {
                document.getElementById(fields[0]).innerHTML = fields[0];
                document.getElementById(fields[0]+1).innerHTML = fields[1];
                document.getElementById(fields[0]+2).innerHTML = fields[2];
                if (fields[3] == "UP") {
                    document.getElementById(fields[0]+3).innerHTML =
                        "<img src='resources/up_g.gif'/>";
                } else if (fields[3] == "DOWN") {
                    document.getElementById(fields[0]+3).innerHTML =
                        "<img src='resources/down_r.gif'/>";
                } else {
                    document.getElementById(fields[0]+3).innerHTML = "";
                }
            } else {
                newRow = stockTable.insertRow(stockTable.rows.length);
                newCell = newRow.insertCell(newRow.cells.length);
                newCell.id = fields[0];
                newCell.innerHTML = fields[0];
                newCell.align = "center";
                newCell.width = "20%";
                newCell = newRow.insertCell(newRow.cells.length);
                newCell.id = fields[0]+1;
                newCell.innerHTML = fields[1];
                newCell.align = "center";
                newCell.width = "20%";
                newCell = newRow.insertCell(newRow.cells.length);
                newCell.id = fields[0]+2;
                newCell.innerHTML = fields[2];
                newCell.align = "center";
                newCell.width = "20%";
                newCell = newRow.insertCell(newRow.cells.length);
                newCell.id = fields[0]+3;
                if (fields[3] == "UP") {
                    document.getElementById(fields[0]+3).innerHTML =
                        "<img src='resources/up_g.gif'/>";
                } else if (fields[3] == "DOWN") {
                    document.getElementById(fields[0]+3).innerHTML =
                        "<img src='resources/down_r.gif'/>";
                } else {
                    document.getElementById(fields[0]+3).innerHTML = "";
                }

                newCell.align = "center";
                newCell.width = "20%";
            }
        }
    }
}

function clockHandler(event) {
    var eventData = event.data.toString().replace(/^\s*/,"").replace(/\s*$/,"");
    var clock = document.getElementById("clock");
    clock.innerHTML = eventData;
}

function rssHandler(event) {
    var eventData = event.data.toString().replace(/^\s*/,"").replace(/\s*$/,"");
    var rssInfo = document.getElementById("rssInfo");
    if (eventData.length == 0) {
        rssInfo.style.visibility="hidden";
    } else {
        rssInfo.style.visibility="visible";
    }
    rssInfo.innerHTML="";
    var rssArray = eventData.split("*****");
    for (var i = 0; i <= rssArray.length; i++) {
        rssInfo.innerHTML += rssArray[i] + "<br/>";
    }
    
}

function clearTableRows(stockTable, rowCount) {
    for (var i = 1; i <= rowCount; i++) {
        stockTable.deleteRow(i);
    }
}
