Ext.define('KitchenSink.view.materialsReview.GSMmaterialsReview.materialsReviewViewAppJudgeWindow', {
    extend: 'Ext.window.Window',
    xtype: 'GSMmaterialsReviewAppJugDetail',
    reference: 'GSMmaterialsReviewAppJugDetail',
    controller: 'GSMmaterialsReview',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'KitchenSink.view.materialsReview.GSMmaterialsReview.materialsReviewViewAppJudgeStore'
    ],
    title: '查看申请人评审结果',
    width: 600,
    height: 400,
    modal:true,
    layout: {
        type: 'fit'
    },
    scoreType:"" ,
    constructor: function(scoreType){
        this.scoreType = scoreType;
        this.callParent();
    },
    initComponent:function(){
        var scoreType = this.scoreType;
        var scoreItems = [ {
            text: "评委姓名",
            dataIndex: 'judgeRealName',
            minWidth: 100

        }, {
                text: "申请人姓名",
                dataIndex: 'studentRealName',
                width: 100
            }];
        if(scoreType.length==1){
            tItems={
                text: scoreType[0],
                name: scoreType[0],
                align: 'center',
                dataIndex: 'score',
                flex:1,
                renderer: function (v, metaData) {
                    var headText = metaData.column.name,
                        resultHTML = "";
                    for (var x = 0; x < v.length; x++) {
                        if (v[x].name == headText) {
                            resultHTML = v[x].value;
                        }
                    }
                    //console.log(resultHTML)

                    return "<span title='"+resultHTML+"'>"+resultHTML+"</span>";
                }
            }
            scoreItems.push(tItems);
        }
        else {
            //是否存在备注说明，若存在，放在列最后
            var BeiZhuShuoMing=0;
            for (var i = 0; i < scoreType.length; i++) {
                var flex;
                //若为说明，则先不加入评分列
                if(scoreType[i]=="备注说明"||scoreType[i]=="备注"){
                    BeiZhuShuoMing=scoreType[i];
                }else{
                    if(scoreType[i]=="评语"){
                        flex=2
                    }else{ flex=1}
                    var tItems = {
                        text: scoreType[i],
                        name: scoreType[i],
                        align: 'center',
                        dataIndex: 'score',
                        flex:flex,
                        renderer: function (v, metaData) {

                            var headText = metaData.column.name,
                                resultHTML = "";
                            for (var x = 0; x < v.length; x++) {
                                if (v[x].name == headText) {
                                    resultHTML = v[x].value;
                                }
                            }
                            //console.log(resultHTML)

                            return "<span title='"+resultHTML+"'>"+resultHTML+"</span>";
                        }
                    };
                    scoreItems.push(tItems);}
            }


            if(BeiZhuShuoMing!=0){
                var ShuoMingtItems = {
                    text: BeiZhuShuoMing,
                    name: BeiZhuShuoMing,
                    align: 'center',
                    dataIndex: 'score',
                    flex:flex,
                    renderer: function (v, metaData) {

                        var headText = metaData.column.name,
                            resultHTML = "";
                        for (var x = 0; x < v.length; x++) {
                            if (v[x].name == headText) {
                                resultHTML = v[x].value;
                            }
                        }
                        //console.log(resultHTML)

                        return "<span title='"+resultHTML+"'>"+resultHTML+"</span>";
                    }
                };
                scoreItems.push(ShuoMingtItems);
            }

        }


        Ext.apply(this,{
            items: [{
                xtype: 'grid',
                autoHeight:true,
                //xtype: 'grouped-header-grid',
                columnLines: true,
                frame: true,
                style:'border:0',
                store: {
                    type:'GSMmaterialsReviewViewAppJudgeStore'
                },
                columns: scoreItems
//                     {
//                       text:'评委评审得分   ',
//                       // dataIndex:'score',
//                        minWidth:250,
//                        columns:scoreItems
//
//                    }

//        listeners: {
//            afterRender: function(grid) {
//                grid.store.load();
//            }
//        }
            }]
        })
        this.callParent();
    }

});