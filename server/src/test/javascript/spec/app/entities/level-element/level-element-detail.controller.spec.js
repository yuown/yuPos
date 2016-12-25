'use strict';

describe('Controller Tests', function() {

    describe('LevelElement Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockLevelElement, MockLevel, MockElement;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockLevelElement = jasmine.createSpy('MockLevelElement');
            MockLevel = jasmine.createSpy('MockLevel');
            MockElement = jasmine.createSpy('MockElement');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'LevelElement': MockLevelElement,
                'Level': MockLevel,
                'Element': MockElement
            };
            createController = function() {
                $injector.get('$controller')("LevelElementDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'yuPosApp:levelElementUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
