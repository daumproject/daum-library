
function findRowIndexUsingCol(StringToCheckFor, tableToCheck, iColumn)
{
    var i, c, oTable, aaRows;
    oTable = $(tableToCheck).dataTable();
    aaRows = oTable.fnGetData();

    for ( i=0, c=aaRows.length ; i<c ; i++ )
    {
        if ( aaRows[i][iColumn] == StringToCheckFor )
        {
            return i;
        }
    }
    return -1;
}


function timeConverter(UNIX_timestamp){
 var a = new Date(UNIX_timestamp*1000);
 var months = ['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec'];
     var year = a.getFullYear();
     var month = months[a.getMonth()];
     var date = a.getDate();
     var hour = a.getHours();
     var min = a.getMinutes();
     var sec = a.getSeconds();
     var time = date+','+month+' '+year+' '+hour+':'+min+':'+sec ;
     return time;
 }