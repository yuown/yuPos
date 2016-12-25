(function() {
    'use strict';
    angular
        .module('yuPosApp')
        .factory('Configuration', Configuration);

    Configuration.$inject = ['$resource'];

    function Configuration ($resource) {
        var resourceUrl =  'api/configurations/:id';

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
