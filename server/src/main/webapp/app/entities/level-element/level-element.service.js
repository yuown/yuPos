(function() {
    'use strict';
    angular
        .module('yuPosApp')
        .factory('LevelElement', LevelElement);

    LevelElement.$inject = ['$resource'];

    function LevelElement ($resource) {
        var resourceUrl =  'api/level-elements/:id';

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
