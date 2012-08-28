/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package paint;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import paint.*;
import java.lang.String;

public function saveXML(nodes :  MyShape[]){

    var docFactory :DocumentBuilderFactory  = DocumentBuilderFactory.newInstance();
    var docBuilder :DocumentBuilder  = docFactory.newDocumentBuilder();

    var doc	: Document  = docBuilder.newDocument();
    var rootElement: Element  = doc.createElement("paint");
    doc.appendChild(rootElement);


    for (node in nodes)
    {
        if (node instanceof MyCircle)
        {
            var Mycircle : Element  = doc.createElement("Mycircle");
            rootElement.appendChild(Mycircle);

            var temp = node as MyCircle;

            var x=temp.centerX;
            var centerX:  Element  = doc.createElement("centerX");
            centerX.appendChild(doc.createTextNode("{x}"));
            Mycircle.appendChild(centerX);

            x=temp.centerY;
            var centerY:  Element  = doc.createElement("centerY");
            centerX.appendChild(doc.createTextNode("{x}"));
            Mycircle.appendChild(centerY);

            x=temp.radius;
            var radius:  Element  = doc.createElement("radius");
            centerX.appendChild(doc.createTextNode("{x}"));
            Mycircle.appendChild(radius);

            x=temp.translateX;
            var translateX:  Element  = doc.createElement("translateX");
            translateX.appendChild(doc.createTextNode("{x}"));
            Mycircle.appendChild(translateX);

            x=temp.translateY;
            var translateY:  Element  = doc.createElement("translateY");
            translateY.appendChild(doc.createTextNode("{x}"));
            Mycircle.appendChild(translateY);

            var color=temp.fill;
            var fill:  Element  = doc.createElement("fill");
            fill.appendChild(doc.createTextNode("{color}"));
            Mycircle.appendChild(fill);


        }
        else if (node instanceof MyRectangle )
        {
            var MyRectangle : Element  = doc.createElement("");
            rootElement.appendChild(MyRectangle);

            var temp = node as MyRectangle;

            var f=temp.x;
            var x:  Element  = doc.createElement("x");
            x.appendChild(doc.createTextNode("{f}"));
            MyRectangle.appendChild(x);

            f=temp.y;
            var y:  Element  = doc.createElement("y");
            y.appendChild(doc.createTextNode("{f}"));
            MyRectangle.appendChild(y);

            f=temp.width;
            var width:  Element  = doc.createElement("width");
            width.appendChild(doc.createTextNode("{f}"));
            MyRectangle.appendChild(width);

            f=temp.height;
            var height:  Element  = doc.createElement("height");
            height.appendChild(doc.createTextNode("{f}"));
            MyRectangle.appendChild(height);

            f=temp.translateX;
            var translateX:  Element  = doc.createElement("translateX");
            translateX.appendChild(doc.createTextNode("{f}"));
            MyRectangle.appendChild(translateX);

            f=temp.translateY;
            var translateY:  Element  = doc.createElement("translateY");
            translateY.appendChild(doc.createTextNode("{f}"));
            MyRectangle.appendChild(translateY);

            var color=temp.fill;
            var fill:  Element  = doc.createElement("fill");
            fill.appendChild(doc.createTextNode("{color}"));
            MyRectangle.appendChild(fill);

        }
        else if (node instanceof MySquare )
        {
            var MySquare : Element  = doc.createElement("");
            rootElement.appendChild(MySquare);

            var temp = node as MySquare;

            var f=temp.x;
            var x:  Element  = doc.createElement("x");
            x.appendChild(doc.createTextNode("{f}"));
            MySquare.appendChild(x);

            f=temp.y;
            var y:  Element  = doc.createElement("y");
            y.appendChild(doc.createTextNode("{f}"));
            MySquare.appendChild(y);

            f=temp.side;
            var side:  Element  = doc.createElement("side");
            side.appendChild(doc.createTextNode("{f}"));
            MySquare.appendChild(side);

            f=temp.translateX;
            var translateX:  Element  = doc.createElement("translateX");
            translateX.appendChild(doc.createTextNode("{f}"));
            MySquare.appendChild(translateX);

            f=temp.translateY;
            var translateY:  Element  = doc.createElement("translateY");
            translateY.appendChild(doc.createTextNode("{f}"));
            MySquare.appendChild(translateY);

            var color=temp.fill;
            var fill:  Element  = doc.createElement("fill");
            fill.appendChild(doc.createTextNode("{color}"));
            MySquare.appendChild(fill);


        }
        else if (node instanceof MyEllipse )
        {
            var MyEllipse : Element  = doc.createElement("");
            rootElement.appendChild(MyEllipse);

            var temp = node as MyEllipse;

            var f=temp.centerX;
            var centerX:  Element  = doc.createElement("centerX");
            centerX.appendChild(doc.createTextNode("{f}"));
            MyEllipse.appendChild(centerX);

            f=temp.centerY;
            var centerY:  Element  = doc.createElement("centerY");
            centerY.appendChild(doc.createTextNode("{f}"));
            MyEllipse.appendChild(centerY);

            f=temp.radiusX;
            var radiusX:  Element  = doc.createElement("radiusX");
            radiusX.appendChild(doc.createTextNode("{f}"));
            MyEllipse.appendChild(radiusX);

            f=temp.radiusY;
            var radiusY:  Element  = doc.createElement("radiusY");
            radiusY.appendChild(doc.createTextNode("{f}"));
            MyEllipse.appendChild(radiusY);

            f=temp.translateX;
            var translateX:  Element  = doc.createElement("translateX");
            translateX.appendChild(doc.createTextNode("{f}"));
            MyEllipse.appendChild(translateX);

            f=temp.translateY;
            var translateY:  Element  = doc.createElement("translateY");
            translateY.appendChild(doc.createTextNode("{f}"));
            MyEllipse.appendChild(translateY);

            var color=temp.fill;
            var fill:  Element  = doc.createElement("fill");
            fill.appendChild(doc.createTextNode("{color}"));
            MyEllipse.appendChild(fill);


         }
        else if (node instanceof MyLine )
        {
            var MyLine : Element  = doc.createElement("");
            rootElement.appendChild(MyLine);

            var temp = node as MyLine;

            var f=temp.startX;
            var startX:  Element  = doc.createElement("startX");
            startX.appendChild(doc.createTextNode("{f}"));
            MyLine.appendChild(startX);

            f=temp.startY;
            var startY:  Element  = doc.createElement("startY");
            startY.appendChild(doc.createTextNode("{f}"));
            MyLine.appendChild(startY);

            f=temp.endX;
            var endX:  Element  = doc.createElement("endX");
            endX.appendChild(doc.createTextNode("{f}"));
            MyLine.appendChild(endX);

            f=temp.endY;
            var endY:  Element  = doc.createElement("endY");
            endY.appendChild(doc.createTextNode("{f}"));
            MyLine.appendChild(endY);

            f=temp.translateX;
            var translateX:  Element  = doc.createElement("translateX");
            translateX.appendChild(doc.createTextNode("{f}"));
            MyLine.appendChild(translateX);

            f=temp.translateY;
            var translateY:  Element  = doc.createElement("translateY");
            translateY.appendChild(doc.createTextNode("{f}"));
            MyLine.appendChild(translateY);

            var color=temp.fill;
            var fill:  Element  = doc.createElement("fill");
            fill.appendChild(doc.createTextNode("{color}"));
            MyLine.appendChild(fill);


         }
         else if (node instanceof MyTriangle )
        {
            var MyTriangle : Element  = doc.createElement("");
            rootElement.appendChild(MyTriangle);

            var temp = node as MyTriangle;

            var f=temp.points[0];
            var points0:  Element  = doc.createElement("points0");
            points0.appendChild(doc.createTextNode("{f}"));
            MyTriangle.appendChild(points0);

            f=temp.points[1];
            var points1:  Element  = doc.createElement("points1");
            points1.appendChild(doc.createTextNode("{f}"));
            MyTriangle.appendChild(points1);

            f=temp.points[2];
            var points2:  Element  = doc.createElement("points2");
            points2.appendChild(doc.createTextNode("{f}"));
            MyTriangle.appendChild(points2);

            f=temp.points[3];
            var points3:  Element  = doc.createElement("points3");
            points3.appendChild(doc.createTextNode("{f}"));
            MyTriangle.appendChild(points3);

            f=temp.points[4];
            var points4:  Element  = doc.createElement("points4");
            points4.appendChild(doc.createTextNode("{f}"));
            MyTriangle.appendChild(points4);

            f=temp.points[5];
            var points5:  Element  = doc.createElement("points5");
            points5.appendChild(doc.createTextNode("{f}"));
            MyTriangle.appendChild(points5);

            f=temp.translateX;
            var translateX:  Element  = doc.createElement("translateX");
            translateX.appendChild(doc.createTextNode("{f}"));
            MyTriangle.appendChild(translateX);

            f=temp.translateY;
            var translateY:  Element  = doc.createElement("translateY");
            translateY.appendChild(doc.createTextNode("{f}"));
            MyTriangle.appendChild(translateY);

            var color=temp.fill;
            var fill:  Element  = doc.createElement("fill");
            fill.appendChild(doc.createTextNode("{color}"));
            MyTriangle.appendChild(fill);



         }
    }
                     //write the content into xml file
    var transformerFactory:  TransformerFactory  = TransformerFactory.newInstance();
    var transformer:  Transformer  = transformerFactory.newTransformer();
    var source:  DOMSource  = new DOMSource(doc);
    var result:  StreamResult  =  new StreamResult(new File("test.xml"));
    transformer.transform(source, result);

            }

function run(){
    saveXML([MyCircle{radius:30}])
    }