<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%@ page contentType="text/html" %>
<%@ page pageEncoding="ISO-8859-1" %> 

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
	<head>
		<title><s:message code="corp.name"/></title>

		<link rel="icon" type="image/png" href="<c:url value="/favicon.ico"/>" />

		<link href="<c:url value="/static/css/defaults.css"/>" media="screen" rel="stylesheet" type="text/css" />
		<link href="<c:url value="/static/css/application.css"/>" media="screen" rel="stylesheet" type="text/css" />

		<script type="text/javascript" src="<c:url value="/static/js/jquery/jquery-1.3.1.min.js"/>"></script>
	    <script type="text/javascript" src="<c:url value="/static/js/utilities.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/static/js/wz/wz_jsgraphics.js"/>"></script>

		<script language="JavaScript" src="<c:url value="/static/js/jsviz/physics/ParticleModel.js"/>"></script>
		<script language="JavaScript" src="<c:url value="/static/js/jsviz/layout/graph/ForceDirectedLayout.js"/>"></script>

		<script language="JavaScript" src="<c:url value="/static/js/jsviz/physics/Magnet.js"/>"></script>
		<script language="JavaScript" src="<c:url value="/static/js/jsviz/physics/Spring.js"/>"></script>
		<script language="JavaScript" src="<c:url value="/static/js/jsviz/physics/Particle.js"/>"></script>
		<script language="JavaScript" src="<c:url value="/static/js/jsviz/physics/RungeKuttaIntegrator.js"/>"></script>
		
		<script language="JavaScript" src="<c:url value="/static/js/jsviz/layout/view/HTMLGraphView.js"/>"></script>
		<script language="JavaScript" src="<c:url value="/static/js/jsviz/layout/view/GraphView.js"/>"></script>
		
		<script language="JavaScript" src="<c:url value="/static/js/jsviz/util/Timer.js"/>"></script>
		<script language="JavaScript" src="<c:url value="/static/js/jsviz/util/EventHandler.js"/>"></script>
		
		<script language="JavaScript" src="<c:url value="/static/js/jsviz/io/DataGraph.js"/>"></script>
		

		<script type="text/javascript">
			var context = '<c:url value="/"/>';
			
			/* Model 2 JS */
			var model = {};
			<c:if test="${model!=null}">
			    <c:forEach var="r" items="${model}">
			    model['<c:out value="${r.key}"/>'] = <c:out value="${fn:substring(r.value, fn:indexOf(r.value, ':')+1, fn:length(r.value)-1 )}" escapeXml="false"/>;
			    </c:forEach>
			</c:if>
		</script>

		<script type="text/javascript" >

		var jg;
		$(document).ready(function(){

			$.ajax({
			    type: 'GET',
			    url: url('data/person/' + model.personId + '/net'),
			    dataType: 'json',
			    success: function(data){
					initJsViz(data.net);
			    }
			});
			    
		});

		function initJsViz(net) {

			/* 1) Create a new SnowflakeLayout.
			 * 
			 * If you're going to place the graph in an HTML Element, other
			 * the <body>, remember that it must have a known size and
			 * position (via element.offsetWidth, element.offsetHeight,
			 * element.offsetTop, element.offsetLeft).
			 */
			var layout = new ForceDirectedLayout( document.body, true );

						var layout = new ForceDirectedLayout( document.body,
							{	skew: true,
								useCanvas: false,
								useVector: false,
								edgeRenderer: "html"
							});


			layout.view.skewBase=200;
			layout.setSize();

			layout.config._default = {
				model: function( dataNode ) {
					return {
						mass: .5
					}
				},
				view: function( dataNode, modelNode ) {
					var nodeElement = $('<div id="node_' + dataNode.id + '" class="netGraphNode">');
					if (dataNode.class) {
						nodeElement.addClass(dataNode.class);
					}
					
					if (dataNode.innerHTML) {
						nodeElement.append(dataNode.innerHTML);
					} else {
						if (dataNode.title) {
							nodeElement[0].innerHTML="<p>"+dataNode.title+"</p>";
						} else {
							nodeElement[0].innerHTML="<p></p>";
						}
					}
					//nodeElement.onmousedown =  new EventHandler( layout, layout.handleMouseDownEvent, modelNode.id )
					return nodeElement[0];
				}
			}

		    layout.forces.spring._default = function( nodeA, nodeB, isParentChild ) {
				return {
					springConstant: 0.2,
					dampingConstant: 0.2,
					restLength: 20
				}
			}

			layout.forces.spring['ROOT'] = {};
			layout.forces.spring['ROOT']['FOLLOWERS_NODE'] = function( nodeA, nodeB, isParentChild ) {
				return {
					springConstant: 0.8,
					dampingConstant: 0.2,
					restLength: 10
				}
			}
			layout.forces.spring['ROOT']['FOLLOWING_NODE'] = function( nodeA, nodeB, isParentChild ) {
				return {
					springConstant: 0.8,
					dampingConstant: 0.2,
					restLength: 10
				}
			}

			layout.forces.spring['PERSON'] = {};
			layout.forces.spring['PERSON']['PERSON'] = function( nodeA, nodeB, isParentChild ) {
				return {
					springConstant: 0.8,
					dampingConstant: 0.2,
					restLength: 60
				}
			}

		    layout.forces.magnet = function() {
				return {
					magnetConstant: -4000,
					minimumDistance: 20
				}
			}
			
			/* 3) Override the default edge properties builder.
			 * 
			 * @return DOMElement
			 */ 
			layout.viewEdgeBuilder = function( dataNodeSrc, dataNodeDest ) {
				return {
					//0.3.4
					'html_pixels': 5,
					'stroke': dataNodeSrc.color,
					'stroke-width': '2px',
					'stroke-dasharray': '2,4',
					//0.3.3
					'pixelColor': dataNodeSrc.color,
					'pixelWidth': '2px',
					'pixelHeight': '2px',
					'pixels': 5
				}
		/*
				return {
					'pixelColor': dataNodeSrc.color,
					'pixelWidth': '2px',
					'pixelHeight': '2px',
					'pixels': 5
				}
		*/
			}

			/* 4) Load up some stuff by hand
			 * 
			 */

			//console.log(net);
			
			layout.model.ENTROPY_THROTTLE=true;
			
			colors = new Dictionary();
			colors.put("PERSON", "#0000ff");
			colors.put("GROUP", "#ff0000");

			classes = new Dictionary();
			classes.put("PERSON", "netGraphPerson");
			classes.put("GROUP", "netGraphGroup");
			classes.put("GROUP_NODE", "netGraphGroupNode");
			classes.put("FOLLOWING_NODE", "netGraphFollowingNode");
			classes.put("FOLLOWERS_NODE", "netGraphFollowersNode");

			titles = new Dictionary();
			titles.put("FOLLOWING_NODE", "I'M FOLLOWING");
			titles.put("FOLLOWERS_NODE", "FOLLOWING ME");
			titles.put("GROUP_NODE", "GROUPS");

			nodesReg = new Dictionary();
			
			// Add Nodes
		    var nodes = [].concat(net.nodes||[]);
		    $.each(nodes, function(i, node) {
				var gnode = new DataGraphNode();
				gnode.fixed = false; //node.fixed;
				gnode.title = node.title;
				gnode.id = node.id;
				gnode.color="#8888bb";
				gnode.type = node.type;
				gnode.parent = false;
				eval("gnode.data = " + node.data);

				if(colors.get(node.type)) {
					gnode.color=colors.get(node.type);
				}
				if (classes.get(node.type)) {
					gnode.class = classes.get(node.type);
				}
				if (titles.get(node.type)) {
					gnode.title = titles.get(node.type);
				}

				if (node.type=="PERSON"||node.type=="ROOT") {
					gnode.innerHTML = '<img class="mugshot" src="' + profileImgUrl(node.id.substring(1), gnode.data.hasPicture)+ '" title="' + node.title + '" />';
				} 

				layout.newDataGraphNode( gnode );

				nodesReg.put(node.id, gnode);
		    });
			
			// Add relations
			$.each(nodes, function(i, node) {
				gnode = nodesReg.get(node.id);
				var connections = [].concat(node.connections||[]);
				$.each(connections, function(i, connection) {
					gdest = nodesReg.get(connection.destinationId);
					layout.newDataGraphEdge(gnode, gdest);
				});
			});

			/* 5) Control the addition of nodes and edges with a timer.
			 * 
			 * This enables the graph to start organizng as data is loaded.
			 * Use a larger tick time for smoother animation, but slower
			 * build time.
			 */
			var buildTimer = new Timer( 0 );
			buildTimer.subscribe( layout );
			buildTimer.start();
		}
		</script>

	</head>
	<body style="background-color:#fff">
	</body>
</html>

		