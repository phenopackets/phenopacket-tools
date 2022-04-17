package org.phenopackets.phenopackettools.html;

/**
 * Some CSS and boilerplate HTML for writing phenopackets.
 * @author Peter N Robinson
 */
public class HtmlUtil {


    public static String header(String phenopacketId) {
        return "<!doctype html>\n" +
                "<html class=\"no-js\" lang=\"\">\n" +
                "<head>\n" +
                "<meta charset=\"utf-8\">\n" +
                "<meta http-equiv=\"x-ua-compatible\" content=\"ie=edge\">\n" +
                "<title>Phenopacket: " + phenopacketId + "</title>\n" +
                "<meta name=\"description\" content=\"\">\n" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">\n" +
                css() +
                "\n</head>\n";
    }


    private static String css() {
        return "<style>\n" +
                "*{\n" +
                "    -moz-box-sizing: border-box;\n" +
                "    -webkit-box-sizing: border-box;\n" +
                "    box-sizing: border-box\n" +
                "}\n" +
                "html, body, h1, li, a, article, aside, footer, header, main, nav, section {\n" +
                "    padding: 0;\n" +
                "    margin: 0;\n" +
                "}\n" +
                "html, body {\n" +
                "    font-size:14px;\n" +
                "}\n" +
                "body {\n" +
                "    font-family:\"DIN Next\", Helvetica, Arial, sans-serif;\n" +
                "    line-height:1.25;\n" +
                "    background-color: white;\n" +
                "}\n" +
                "body > header, nav, main, body > section, footer {\n" +
                "max-width:1200px;\n" +
                "margin-left:auto;\n" +
                "margin-right:auto;\n" +
                "}\n" +
                "@media(min-width:1440px) {\n" +
                "body > header, nav, main, body > section, footer {\n" +
                "    width:90%;\n" +
                "    max-width:unset;\n" +
                "    }\n" +
                "}\n" +
                "main, body > section {\n" +
                "    margin-top:1.5rem;\n" +
                "    margin-bottom:1.5rem;\n" +
                "}\n" +
                "body > header, body > section {\n" +
                "    padding:2.1rem 2rem 1.6rem;\n" +
                "}\n" +
                ".fr {\n" +
                "  float: right;\n" +
                "}\n" +
                "a[href] {\n" +
                "    color:#05396b;\n" +
                "}\n" +
                "a[href]:hover {\n" +
                "    color:#009ed0;\n" +
                "}\n" +
                "p {\n" +
                "    padding:0;\n" +
                "    margin:0.75rem 0;\n" +
                "}\n" +
                "h1 {\n" +
                "    font-family:\"DIN Next\", Helvetica, Arial, sans-serif;\n" +
                "    font-weight:700;\n" +
                "    font-size:1.8rem;\n" +
                "    line-height:1;\n" +
                "}\n" +
                ".center {\n" +
                "  text-align: center;\n" +
                "}\n" +
                "main > section > a[name=\"othergenes\"] > h3,\n" +
                "h2 {\n" +
                "    font-family:\"DIN Next\", Helvetica, Arial, sans-serif;\n" +
                "    font-weight:700;\n" +
                "    font-size:1.5rem;\n" +
                "    line-height:1;\n" +
                "    margin:0 0 0.5rem;\n" +
                "    padding:0;\n" +
                "}\n" +
                "h3 {\n" +
                "    font-family:\"DIN Next\", Helvetica, Arial, sans-serif;\n" +
                "    font-weight:700;\n" +
                "    font-size:1.2rem;\n" +
                "    line-height:1;\n" +
                "    margin:0 0 0.5rem;\n" +
                "    padding:0;\n" +
                "}\n" +
                "main ul, main ol {\n" +
                "    margin:0.5rem 0 0.5rem 1.4rem;\n" +
                "    padding:0;\n" +
                "}\n" +
                "main li {\n" +
                "    margin:0.25rem 0;\n" +
                "    padding:0;\n" +
                "}\n" +
                ".banner {\n" +
                "    background-color: #05396b;\n" +
                "    color: white;\n" +
                "}\n" +
                "nav {\n" +
                "    background-color: #4DA8DA;\n" +
                "    margin-top:1px;\n" +
                "    overflow:auto;\n" +
                "    zoom:1;\n" +
                "    padding:0;\n" +
                "}\n" +
                "nav a[href] {\n" +
                "    color:white;\n" +
                "    text-decoration:none;\n" +
                "    color:rgba(255,255,255,0.8);\n" +
                "    font-size:1.2rem;\n" +
                "    display:block;\n" +
                "    padding:1rem;\n" +
                "    font-weight:400;\n" +
                "}\n" +
                "nav li:last-child a[href] {\n" +
                "    padding-right:2.25rem;\n" +
                "}\n" +
                "nav a[href]:hover {\n" +
                "    color:#05396b;\n" +
                "    background-color:#04c3ff;\n" +
                "}\n" +
                "#navi ul {\n" +
                "    display:table;\n" +
                "    float:right;\n" +
                "    margin:0;\n" +
                "}\n" +
                "#navi li {\n" +
                "    display:block;\n" +
                "    float:left;\n" +
                "}\n" +
                "main > section:first-child {\n" +
                "    margin-top:1.5rem;\n" +
                "    margin-bottom:1.5rem;\n" +
                "    background-color:white;\n" +
                "    padding:2.1rem 2rem 1.6rem;\n" +
                "}\n" +
                "main > section {\n" +
                "    margin-top:1.5rem;\n" +
                "    margin-bottom:0;\n" +
                "    background-color:white;\n" +
                "    padding: .5rem;\n" +
                "}\n" +
                "main > section > article {\n" +
                "    padding: 1.5rem;\n" +
                "    margin-top:1px;\n" +
                "    background-color:white;\n" +
                "}\n" +
                "table {\n" +
                "    border-collapse: collapse;\n" +
                "    width:100%;\n" +
                "    margin:0.5rem 0;\n" +
                "}\n" +
                "th, td {\n" +
                "    text-align:left;\n" +
                "    padding:0.4rem 0.5rem 0.25rem;\n" +
                "}\n" +
                "th {\n" +
                "    background-color: #e0e3ea;\n" +
                "    border-bottom:1px solid white;\n" +
                "}" +
                "table.minimalistBlack {\n" +
                "  border: 3px solid #000000;\n" +
                "  width: 100%;\n" +
                "  text-align: left;\n" +
                "  border-collapse: collapse;\n" +
                "}\n" +
                "table.minimalistBlack td, table.minimalistBlack th {\n" +
                "  border: 1px solid #000000;\n" +
                "  padding: 5px 4px;\n" +
                "}\n" +
                "table.minimalistBlack tbody td {\n" +
                "  font-size: 13px;\n" +
                "}\n" +
                "table.minimalistBlack thead {\n" +
                "  background: #CFCFCF;\n" +
                "  background: -moz-linear-gradient(top, #dbdbdb 0%, #d3d3d3 66%, #CFCFCF 100%);\n" +
                "  background: -webkit-linear-gradient(top, #dbdbdb 0%, #d3d3d3 66%, #CFCFCF 100%);\n" +
                "  background: linear-gradient(to bottom, #dbdbdb 0%, #d3d3d3 66%, #CFCFCF 100%);\n" +
                "  border-bottom: 3px solid #000000;\n" +
                "}\n" +
                "table.minimalistBlack thead th {\n" +
                "  font-size: 15px;\n" +
                "  font-weight: bold;\n" +
                "  color: #000000;\n" +
                "  text-align: left;\n" +
                "}\n" +
                "table.minimalistBlack tfoot {\n" +
                "  font-size: 14px;\n" +
                "  font-weight: bold;\n" +
                "  color: #000000;\n" +
                "  border-top: 3px solid #000000;\n" +
                "}\n" +
                "table.minimalistBlack tfoot td {\n" +
                "  font-size: 14px;\n" +
                "}"+
                "\n</style>\n";
    }


    public static String htmlTop() {
        return "<body>\n" +
                "<header class=\"banner\">\n" +
                "<h1><font color=\"#FFDA1A\">Phenopacket-Tools</font></h1>\n" +
                "</header>\n" +
                "<main>\n";
    }

    public static String htmlBottom() {
        return " <span id=\"tooltip\" display=\"none\" style=\"position: absolute; display: none;\"></span>\n" +
                "</main>\n" +
                "</body>\n" +
                "</html>";
    }


}
