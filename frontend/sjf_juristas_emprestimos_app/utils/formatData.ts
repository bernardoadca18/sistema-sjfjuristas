const formatDate = (date : string) => {
    let newDate = '';
    newDate += date[date.length - 2];
    newDate += date[date.length - 1];
    newDate += '/';
    newDate += date[date.length - 5];
    newDate += date[date.length - 4];
    newDate += '/';
    newDate += date[0];
    newDate += date[1];
    newDate += date[2];
    newDate += date[3];
    // AAAA-MM-DD -> DD/MM/AAAA
    return newDate;
}

export default formatDate;