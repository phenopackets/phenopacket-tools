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
        return """
                <style>
                *{
                    -moz-box-sizing: border-box;
                    -webkit-box-sizing: border-box;
                    box-sizing: border-box
                }
                html, body, h1, li, a, article, aside, footer, header, main, nav, section {
                    padding: 0;
                    margin: 0;
                }
                html, body {
                    font-size:14px;
                }
                body {
                    font-family:"DIN Next", Helvetica, Arial, sans-serif;
                    line-height:1.25;
                    background-color: white;
                }
                body > header, nav, main, body > section, footer {
                max-width:1200px;
                margin-left:auto;
                margin-right:auto;
                }
                @media(min-width:1440px) {
                body > header, nav, main, body > section, footer {
                    width:90%;
                    max-width:unset;
                    }
                }
                main, body > section {
                    margin-top:1.5rem;
                    margin-bottom:1.5rem;
                }
                body > header, body > section {
                    padding:2.1rem 2rem 1.6rem;
                }
                .fr {
                  float: right;
                }
                a[href] {
                    color:#05396b;
                }
                a[href]:hover {
                    color:#009ed0;
                }
                p {
                    padding:0;
                    margin:0.75rem 0;
                }
                h1 {
                    font-family:"DIN Next", Helvetica, Arial, sans-serif;
                    font-weight:700;
                    font-size:1.8rem;
                    line-height:1;
                }
                .center {
                  text-align: center;
                }
                main > section > a[name="othergenes"] > h3,
                h2 {
                    font-family:"DIN Next", Helvetica, Arial, sans-serif;
                    font-weight:700;
                    font-size:1.5rem;
                    line-height:1;
                    margin:0 0 0.5rem;
                    padding:0;
                }
                h3 {
                    font-family:"DIN Next", Helvetica, Arial, sans-serif;
                    font-weight:700;
                    font-size:1.2rem;
                    line-height:1;
                    margin:0 0 0.5rem;
                    padding:0;
                }
                main ul, main ol {
                    margin:0.5rem 0 0.5rem 1.4rem;
                    padding:0;
                }
                main li {
                    margin:0.25rem 0;
                    padding:0;
                }
                .banner {
                    background-color: #05396b;
                    color: white;
                }
                nav {
                    background-color: #4DA8DA;
                    margin-top:1px;
                    overflow:auto;
                    zoom:1;
                    padding:0;
                }
                nav a[href] {
                    color:white;
                    text-decoration:none;
                    color:rgba(255,255,255,0.8);
                    font-size:1.2rem;
                    display:block;
                    padding:1rem;
                    font-weight:400;
                }
                nav li:last-child a[href] {
                    padding-right:2.25rem;
                }
                nav a[href]:hover {
                    color:#05396b;
                    background-color:#04c3ff;
                }
                #navi ul {
                    display:table;
                    float:right;
                    margin:0;
                }
                #navi li {
                    display:block;
                    float:left;
                }
                main > section:first-child {
                    margin-top:1.5rem;
                    margin-bottom:1.5rem;
                    background-color:white;
                    padding:2.1rem 2rem 1.6rem;
                }
                main > section {
                    margin-top:1.5rem;
                    margin-bottom:0;
                    background-color:white;
                    padding: .5rem;
                }
                main > section > article {
                    padding: 1.5rem;
                    margin-top:1px;
                    background-color:white;
                }
                table {
                    border-collapse: collapse;
                    width:100%;
                    margin:0.5rem 0;
                }
                th, td {
                    text-align:left;
                    padding:0.4rem 0.5rem 0.25rem;
                }
                th {
                    background-color: #e0e3ea;
                    border-bottom:1px solid white;
                }table.minimalistBlack {
                  border: 3px solid #000000;
                  width: 100%;
                  text-align: left;
                  border-collapse: collapse;
                }
                table.minimalistBlack td, table.minimalistBlack th {
                  border: 1px solid #000000;
                  padding: 5px 4px;
                }
                table.minimalistBlack tbody td {
                  font-size: 13px;
                }
                table.minimalistBlack thead {
                  background: #CFCFCF;
                  background: -moz-linear-gradient(top, #dbdbdb 0%, #d3d3d3 66%, #CFCFCF 100%);
                  background: -webkit-linear-gradient(top, #dbdbdb 0%, #d3d3d3 66%, #CFCFCF 100%);
                  background: linear-gradient(to bottom, #dbdbdb 0%, #d3d3d3 66%, #CFCFCF 100%);
                  border-bottom: 3px solid #000000;
                }
                table.minimalistBlack thead th {
                  font-size: 15px;
                  font-weight: bold;
                  color: #000000;
                  text-align: left;
                }
                table.minimalistBlack tfoot {
                  font-size: 14px;
                  font-weight: bold;
                  color: #000000;
                  border-top: 3px solid #000000;
                }
                table.minimalistBlack tfoot td {
                  font-size: 14px;
                }
                </style>
                """;
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
