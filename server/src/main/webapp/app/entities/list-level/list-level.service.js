(function() {
    'use strict';
    angular
        .module('yuPosApp')
        .factory('ListLevel', ListLevel);

    ListLevel.$inject = ['$resource'];

    function ListLevel ($resource) {
        var resourceUrl =  'api/list-levels/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
