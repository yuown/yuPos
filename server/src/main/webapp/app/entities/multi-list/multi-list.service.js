(function() {
    'use strict';
    angular
        .module('yuPosApp')
        .factory('MultiList', MultiList);

    MultiList.$inject = ['$resource'];

    function MultiList ($resource) {
        var resourceUrl =  'api/multi-lists/:id';

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
