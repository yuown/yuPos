(function() {
    'use strict';
    angular
        .module('yuPosApp')
        .factory('Element', Element);

    Element.$inject = ['$resource'];

    function Element ($resource) {
        var resourceUrl =  'api/elements/:id';

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
