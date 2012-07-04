
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