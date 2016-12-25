'use strict';

describe('Controller Tests', function() {

    describe('Level Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockLevel, MockListLevel, MockLevelElement;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockLevel = jasmine.createSpy('MockLevel');
            MockListLevel = jasmine.createSpy('MockListLevel');
            MockLevelElement = jasmine.createSpy('MockLevelElement');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Level': MockLevel,
                'ListLevel': MockListLevel,
                'LevelElement': MockLevelElement
            };
            createController = function() {
                $injector.get('$controller')("LevelDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'yuPosApp:levelUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
