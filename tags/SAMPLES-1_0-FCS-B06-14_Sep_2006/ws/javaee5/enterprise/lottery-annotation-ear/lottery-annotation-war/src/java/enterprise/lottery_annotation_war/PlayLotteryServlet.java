/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License).  You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the license at
 * https://glassfish.dev.java.net/public/CDDLv1.0.html or
 * glassfish/bootstrap/legal/CDDLv1.0.txt.
 * See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at glassfish/bootstrap/legal/CDDLv1.0.txt.
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * you own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * Copyright 2006 Sun Microsystems, Inc. All rights reserved.
 */

package enterprise.lottery_annotation_war;

import java.io.IOException;
import java.util.Locale; 
import java.util.ResourceBundle; 

import javax.naming.InitialContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;

import enterprise.lottery_annotation_ejb_stateful.Lottery;
import enterprise.lottery_annotation_ejb_stateless.Dice;


public class PlayLotteryServlet extends HttpServlet {
    
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html");

	Lottery lottery;
        Dice dice;
        int NO_OF_DIGITS = 6;
        ResourceBundle rb = ResourceBundle.getBundle("LocalStrings", Locale.getDefault());

        try {
            InitialContext initContext  = new InitialContext();

            lottery = 
                (Lottery) initContext.lookup("enterprise.lottery_annotation_ejb_stateful.Lottery");
	    dice =
                (Dice) initContext.lookup("enterprise.lottery_annotation_ejb_stateless.Dice");
        } 
        catch (Exception e) { 
            System.out.println(rb.getString("exception_creating_initial_context") +
                ": " + e.toString()); 
            return; 
        } 

	lottery.setName(request.getParameter("lottery_name"));

	for(int i=0; i<NO_OF_DIGITS; i++) {
            lottery.select(dice.play());
        }

	String lotteryName = lottery.getName();
        String lotteryNumber = lottery.getNumber();
	String lotteryDate = lottery.getDate();

        //set the results in the Request object
        request.setAttribute("lottery_name", lottery.getName()); 
        request.setAttribute("lottery_number", lottery.getNumber()); 
        request.setAttribute("lottery_date", lottery.getDate()); 

        // dispatch jsp for output
        response.setContentType("text/html");  
        RequestDispatcher dispatcher = 
            getServletContext().getRequestDispatcher("/LotteryView.jsp"); 
        dispatcher.include(request, response);
        return;
    }


    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
    

    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
    

    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        ResourceBundle rb = ResourceBundle.getBundle("LocalStrings", Locale.getDefault());
        return rb.getString("servlet_description"); 
    }
}
