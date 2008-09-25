Ext.onReady(function(){

    // create the Data Store
    var store = new Ext.data.JsonStore({
        root: 'stream.activities',
//        totalProperty: 'total',
//        idProperty: 'id',

        fields: [
            'iconUrl', 'postedTime', 'body', 'title'
        ],

        // load using script tags for cross domain, if the data in on the same domain as
        // this page, an HttpProxy would be better
        proxy: new Ext.data.HttpProxy({
            url: '/workast/r/stream/1'
            ,method : 'GET'
        })
    });
//    store.setDefaultSort('postedTime', 'desc');


    // pluggable renders
    function renderIconLine(value, p, record){
    	return String.format('<img src="http://localhost:8080/workast/{0}"/>', record.data.iconUrl);
    }
    function renderDataLine(value, p, record){
    	return String.format('{0} ({1})<br/>{2}', record.data.title, record.data.postedTime, record.data.body);
    }

    var pagingBar = new Ext.PagingToolbar({
        pageSize: 25,
        store: store,
        displayInfo: true,
        displayMsg: 'Mostrando {0} - {1} de {2}',
        emptyMsg: "No hay datos"
    });
    
    var grid = new Ext.grid.GridPanel({
        el:'stream',
        width:700,
        height:500,
        //title:'News Feed',
        hideHeaders : true,
        store: store,
        trackMouseOver:false,
        disableSelection:true,
        loadMask: true,

        // grid columns
        columns:[{
            header: "icon",
            dataIndex: 'icon',
            width: 10,
            renderer: renderIconLine,
            sortable: false
        },
        {
            header: "data",
            dataIndex: 'data',
            width: 400,
            renderer: renderDataLine,
            sortable: false
        }],

        // customize view config
        viewConfig: {
            forceFit:true,
            enableRowBody:true
        },

        // paging bar on the bottom
        bbar: pagingBar
    });

    // render it
    grid.render();

    // trigger the data store load
    store.load({params:{start:0, limit:25}});
});



