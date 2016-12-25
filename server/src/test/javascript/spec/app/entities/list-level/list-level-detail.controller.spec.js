'use strict';

describe('Controller Tests', function() {

    describe('ListLevel Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockListLevel, MockMultiList, MockLevel;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockListLevel = jasmine.createSpy('MockListLevel');
            MockMultiList = jasmine.createSpy('MockMultiList');
            MockLevel = jasmine.createSpy('MockLevel');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'ListLevel': MockListLevel,
                'MultiList': MockMultiList,
                'Level': MockLevel
            };
            createController = function() {
                $injector.get('$controller')("ListLevelDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'yuPosApp:listLevelUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
