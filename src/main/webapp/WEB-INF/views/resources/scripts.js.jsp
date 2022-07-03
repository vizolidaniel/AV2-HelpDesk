<%@ page language="java" contentType="text/javascript; charset=UTF-8" pageEncoding="UTF-8"%>
var parseQueryString = function() {
    if (window.location.search.includes('?')) {
        var keysAndValues = window.location.search.replace('?', '').split('&');

        var data = {};

        for (var i = 0; i < keysAndValues.length; i++) {
            var keyAndValue = keysAndValues[i].split('=');
            var key = keyAndValue[0];
            var value = '';
            if (keyAndValue.length > 1) value = keyAndValue[1];

            data[key] = value;
        }

        return data;
    }
    return {};
};
var convertToQueryString = function(data) {
    var entries = Object.entries(data);
    var query = '';

    for (var i = 0; i < entries.length; i++) {
        query += '&' + entries[i][0] + '=' + entries[i][1];
    }

    return query.replace('&','?');
};

var addQueryParam = function(paramName, paramValue) {
    var queryData = parseQueryString();
    queryData[paramName] = paramValue;
    window.location.search = convertToQueryString(queryData);
};
